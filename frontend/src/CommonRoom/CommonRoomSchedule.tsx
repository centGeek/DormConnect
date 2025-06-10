import { useParams } from "react-router-dom";
import { useEffect, useState, useContext } from "react";
import Template from "../Template/Template";
import { UserContext } from "../Context/UserContext";
// @ts-expect-error
import { groupBy } from "lodash";
import getRoomStatusTranslation from "../ReusableComponents/CommonRoomTypes.tsx";
import ErrorPopUp  from "./ErrorPopUp.tsx";

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

// @ts-ignore
function CommonRoomSchedule() {
    const { id } = useParams<{ id: string }>();
    const [assignments, setAssignments] = useState<assignmentProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [commonRoom, setCommonRoom] = useState<CommonRoom | null>(null);
    const userContext = useContext(UserContext);
    const [isPopUpErrorOpen, setIsPopUpErrorOpen] = useState<boolean>(false);
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
                    'Authorization': `Bearer ${token}`
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
                const errorMessage = await response.text();
                setIsPopUpErrorOpen(true);
            }
            await fetchAssignments();
        } catch (error) {
            console.error("Error assigning to assignment:", error);
            setIsPopUpErrorOpen(true);
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
    const handleResetAssignments = async (commonRoomId: number) =>{
        try {
            const response = await fetch(`/api/common-room/reset-assignments/${commonRoomId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: "include",
            });
            if (!response.ok) throw new Error("Failed to reset assignments");
            await fetchAssignments();
        } catch (error) {
            console.error("Error resetting assignments:", error);
            alert("Cannot reset assignments.");
        }
    }
    const handleClosePopUp = () => {
        setIsPopUpErrorOpen(false);
    }

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
        <Template buttons={[{text: 'Back', link: '/common-rooms'}]}>
            <header className="bg-gray-200 p-4 rounded-lg shadow-md mb-6">
                {commonRoom && (
                    <div className="text-center text-gray-700">
                        <p className="font-semibold">
                            <span className="text-gray-600">Typ pokoju:</span> {getRoomStatusTranslation(commonRoom.type)}&nbsp;&nbsp;&nbsp;
                            <span className="text-gray-600">Piętro:</span> {commonRoom.floor}&nbsp;&nbsp;&nbsp;
                            <span className="text-gray-600">Pojemność:</span> {commonRoom.capacity}&nbsp;&nbsp;&nbsp;
                            <span className="text-gray-600">Limit zapisów:</span> {commonRoom.timesAWeekYouCanUseIt}
                        </p>
                        {((userContext?.user?.roles.includes("ADMIN") || (userContext?.user?.roles.includes("MANAGER"))) &&
                            <button onClick={() => handleResetAssignments(commonRoom?.id || 0)} className=" mt-4 bg-gray-600 text-white px-4 py-2 rounded-lg shadow hover:bg-gray-400 transition">
                                Resetuj rezerwacje
                            </button>)}
                    </div>
                )}
            </header>
            <div className="flex gap-5 overflow-x-auto max-w-6xl mx-auto p-4">
                {loading ? (
                    <p className="text-center text-gray-500">Ładowanie...</p>
                ) : assignments.length === 0 ? (
                    <p className="text-center text-gray-500">Nie znaleziono żadnych rezerwacji</p>
                ) : (
                    Object.entries(groupBy(assignments, (assignment: assignmentProps) =>
                        new Date(assignment.startDate).toLocaleDateString()
                    )).map(([date, group]) => (
                        <div key={date} className="flex flex-col border border-gray-500 bg-gray-300 p-4 rounded-lg shadow-md min-w-[200px]">
                            <h3 className="text-lg font-bold text-gray-700 mb-3 text-center">{date}</h3>
                            <div className="flex flex-col gap-3">
                                {group.map((assignment: assignmentProps) => (
                                    <div
                                        key={assignment.id}
                                        className={`p-3 rounded-lg shadow-md border text-center hover:bg-gray-200 cursor-pointer transition text-sm ${
                                            assignment.isArchived
                                                ? 'bg-gray-200 text-gray-500 cursor-not-allowed'
                                                : assignment.isUserAssigned
                                                    ? 'bg-green-100 text-green-700'
                                                    : assignment.isFull
                                                        ? 'bg-red-100 text-red-700 cursor-not-allowed'
                                                        : 'bg-white text-gray-700 hover:shadow-lg'
                                        }`}
                                        onClick={() => handleClick(assignment)}
                                    >
                                        <p className="font-semibold">
                                            {new Date(assignment.startDate).toLocaleTimeString([], {
                                                hour: '2-digit',
                                                minute: '2-digit',
                                            })} - {new Date(assignment.endDate).toLocaleTimeString([], {
                                            hour: '2-digit',
                                            minute: '2-digit',
                                        })}
                                        </p>
                                        <p>Zajęte: {assignment.numberOfUsers}/{commonRoom?.capacity}</p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))
                )}
            </div>
            {isPopUpErrorOpen && <ErrorPopUp onClose={handleClosePopUp} />}
        </Template>
    );
}

export default CommonRoomSchedule;