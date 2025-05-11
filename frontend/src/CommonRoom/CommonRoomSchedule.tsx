import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Template from "../Template/Template";
import "./CommonRoomSchedule.css";

interface assignmentProps {
    id: number;
    startDate: string;
    endDate: string;
    numberOfUsers: number;
    isUserAssigned: boolean;
    isFull: boolean;
}

interface CommonRoom {
    id: number;
    type: string;
    floor: number;
    capacity: number;
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
            if (!response.ok) throw new Error('Cannot join the assignment.');
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

    return (
        <Template
            buttons={[{ text: "Powrót", link: "/common-rooms" }]}
            footerContent={<p></p>}
        >
            <header className="common-room-header">
                <h1>Common Room Details</h1>
                {commonRoom && (
                    <div className="common-room-details">
                        <p><strong>Type:</strong> {commonRoom.type}</p>
                        <p><strong>Floor:</strong> {commonRoom.floor}</p>
                        <p><strong>Capacity:</strong> {commonRoom.capacity}</p>
                    </div>
                )}
            </header>
            <div className="common-room-schedule">
                {loading ? (
                    <p>Loading...</p>
                ) : assignments.length === 0 ? (
                    <p>No assignments found</p>
                ) : (
                    assignments.map((assignment: assignmentProps) => (
                        <div
                            key={assignment.id}
                            className={`assignment-card ${assignment.isUserAssigned ? 'assigned' : ''} ${(assignment.isFull && !assignment.isUserAssigned) ? 'full' : ''}`}
                            onClick={() => handleClick(assignment)}
                            style={{ cursor: (assignment.isFull && !assignment.isUserAssigned) ? 'not-allowed' : 'pointer' }}
                        >
                            <p>Start Date: {new Date(assignment.startDate).toLocaleString(undefined, {
                                year: 'numeric',
                                month: '2-digit',
                                day: '2-digit',
                                hour: '2-digit',
                                minute: '2-digit'
                            })}</p>
                            <p>End Date: {new Date(assignment.endDate).toLocaleString(undefined, {
                                year: 'numeric',
                                month: '2-digit',
                                day: '2-digit',
                                hour: '2-digit',
                                minute: '2-digit'
                            })}</p>
                            <p>Number of Users: {assignment.numberOfUsers}/{commonRoom?.capacity}</p>
                        </div>
                    ))
                )}
            </div>
        </Template>
    );
}

export default CommonRoomSchedule;