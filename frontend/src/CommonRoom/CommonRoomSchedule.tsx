import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Template from "../Template/Template";
// @ts-ignore
import { groupBy } from "lodash";
import "./CommonRoomSchedule.css";
import getRoomStatusTranslation from "../ReusableComponents/CommonRoomTypes.tsx";

interface assignmentProps {
    id: number;
    startDate: string;
    endDate: string;
    numberOfUsers: number;
    isUserAssigned: boolean;
    isFull: boolean;
    isArchived: boolean;
}

interface CommonRoom {
    id: number;
    type: string;
    floor: number;
    capacity: number;
    timesAWeekYouCanUseIt: number;
}

function CommonRoomSchedule() {
    const { id } = useParams<{ id: string }>();
    const [assignments, setAssignments] = useState<assignmentProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [commonRoom, setCommonRoom] = useState<CommonRoom | null>(null);
    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    const fetchAssignments = async () => {
        try {
            setLoading(true);
            const response = await fetch(`/api/common-room-assignment/get/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: "include",
            });
            if (!response.ok) throw new Error("Failed to fetch assignments room details");
            const data = await response.json();
            setAssignments(data || []);
        } catch (error) {
            console.error("Error fetching assignments details:", error);
        } finally {
            setLoading(false);
        }
    };

    const fetchCommonRoom = async () => {
        try {
            setLoading(true);

            const response: Response = await fetch(`/api/common-room/get/${id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch common rooms");
            const data = await response.json();
            setCommonRoom(data || null);
        } catch (error: unknown) {
            console.error(
                "Error fetching common rooms:",
                error instanceof Error ? error.message : error
            );
        } finally {
            setLoading(false);
        }
    };

    const handleAssign = async (assignmentId: number) => {
        try {
            const response = await fetch(`/api/common-room-assignment/join/${assignmentId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: "include",
            });
            if (!response.ok) {
                const errorMessage = await response.text(); // Pobierz komunikat błędu
                alert(errorMessage); // Wyświetl powiadomienie użytkownikowi
                return;
            }
            await fetchAssignments();
        } catch (error) {
            console.error("Error assigning to assignment:", error);
            alert("Cannot join the assignment.");
        }
    };

    const handleUnassign = async (assignmentId: number) => {
        try {
            const response = await fetch(`/api/common-room-assignment/leave/${assignmentId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: "include",
            });
            if (!response.ok) throw new Error('Cannot leave the assignment.');
            await fetchAssignments();
        } catch (error) {
            console.error("Error unassigning from assignment:", error);
            alert("Cannot leave the assignment.");
        }
    };

    const handleClick = (assignment: assignmentProps) => {
        if (assignment.isArchived) {
            alert("You cannot join past assignments.");
            return;
        }
        if (assignment.isUserAssigned) {
            handleUnassign(assignment.id);
        } else if (!assignment.isFull) {
            handleAssign(assignment.id);
        }
    };

    useEffect(() => {
        if (id) {
            fetchAssignments();
            fetchCommonRoom();
        }
    }, [id]);

    useEffect(() => {
        const handleScroll = () => {
            sessionStorage.setItem('scrollPosition', window.scrollY.toString());
        };

        window.addEventListener('scroll', handleScroll);

        const savedPosition = sessionStorage.getItem('scrollPosition');
        if (savedPosition) {
            window.scrollTo(0, parseInt(savedPosition, 10));
        }

        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    // @ts-ignore
    return (
        <Template
            buttons={[{ text: "Powrót", link: "/common-rooms" }]}
            footerContent={<p></p>}
        >
            <header className="common-room-header">
                {commonRoom && (
                    <div className="common-room-details">
                        <p><strong>Typ pokoju:</strong> {getRoomStatusTranslation(commonRoom.type)}&nbsp;&nbsp;&nbsp;
                        <strong>Piętro:</strong> {commonRoom.floor}&nbsp;&nbsp;&nbsp;
                        <strong>Pojemność:</strong> {commonRoom.capacity}   &nbsp;&nbsp;&nbsp;
                        <strong>Limit zapisów:</strong> {commonRoom.timesAWeekYouCanUseIt}</p>
                    </div>
                )}
            </header>
            <div className="common-room-schedule">
                {loading ? (
                    <p>Ładowanie...</p>
                ) : assignments.length === 0 ? (
                    <p>Nie znaleziono żadnych rezerwacji</p>
                ) : (
                    Object.entries(groupBy(assignments, (assignment: assignmentProps) =>
                        new Date(assignment.startDate).toLocaleDateString()
                    )).map(([date, group]) => (
                        <div key={date} className="assignment-column">
                            <div className="assignment-column-header">{date}</div>
                            {group.map((assignment: assignmentProps) => (
                                <div
                                    key={assignment.id}
                                    className={`assignment-card ${assignment.isArchived ? 'archived' : ''} ${assignment.isUserAssigned ? 'assigned' : ''} ${(assignment.isFull && !assignment.isUserAssigned) ? 'full' : ''}`}
                                    onClick={() => handleClick(assignment)}
                                    style={{ cursor: assignment.isArchived || (assignment.isFull && !assignment.isUserAssigned) ? 'not-allowed' : 'pointer' }}
                                >
                                    <p>{new Date(assignment.startDate).toLocaleString(undefined, {
                                        hour: '2-digit',
                                        minute: '2-digit'
                                    })} -  {new Date(assignment.endDate).toLocaleString(undefined, {
                                        hour: '2-digit',
                                        minute: '2-digit'
                                    })}</p>
                                    <p>Zajęte: {assignment.numberOfUsers}/{commonRoom?.capacity}</p>
                                </div>
                            ))}
                        </div>
                    ))
                )}
            </div>
        </Template>
    );

}

export default CommonRoomSchedule;