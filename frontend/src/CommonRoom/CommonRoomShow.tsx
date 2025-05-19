import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Template from "../Template/Template.tsx";
import getRoomStatusTranslation from "../ReusableComponents/CommonRoomTypes.tsx";
import getRoomIcon from "../ReusableComponents/CommonRoomIcon.tsx";

interface CommonRoom {
    id: number;
    type: string;
    floor: number;
    capacity: number;
    timesAWeekYouCanUseIt: number;
    isArchived: boolean;
}

function CommonRoomShow() {
    const [loading, setLoading] = useState<boolean>(true);
    const [commonRooms, setCommonRooms] = useState<CommonRoom[]>([]);
    const navigate = useNavigate();

    const fetchCommonRooms = async () => {
        try {
            setLoading(true);

            const response: Response = await fetch('/api/common-room/show', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch common rooms");
            const data = await response.json();
            setCommonRooms(data || []);
        } catch (error: unknown) {
            console.error(
                "Error fetching common rooms:",
                error instanceof Error ? error.message : error
            );
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCommonRooms();
    }, []);

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
        >
            <div className="common-room-show-container flex flex-wrap justify-center gap-6 p-6 bg-gray-200 rounded-lg shadow-lg">
                {loading ? (
                    <p className="text-gray-500">Ładowanie...</p>
                ) : commonRooms.length === 0 ? (
                    <p className="text-gray-500">Nie znaleziono żadnych pokoi</p>
                ) : (
                    commonRooms.map((commonRoom) => (
                        <div
                            key={commonRoom.id}
                            className="common-room-card bg-white border border-gray-500 rounded-lg p-4 w-72 text-center shadow-md hover:shadow-lg transform hover:-translate-y-1 transition duration-300 cursor-pointer"
                            onClick={() => navigate(`/common-room/${commonRoom.id}`)}
                        >
                            <img
                                src={`/src/assets/common_room_icons/${getRoomIcon(commonRoom.type)}.png`}
                                alt={getRoomStatusTranslation(commonRoom.type)}
                                className="h-16 w-16 mx-auto mb-4"
                            />
                            <h2 className="text-lg font-bold text-gray-700 mb-2">
                                {getRoomStatusTranslation(commonRoom.type)}
                            </h2>
                            <p className="text-gray-600">Piętro: {commonRoom.floor}</p>
                        </div>
                    ))
                )}
            </div>
        </Template>
    );
}

export default CommonRoomShow;