import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Template from "../Template/Template.tsx";
import "./CommonRoomShow.css";
import getRoomStatusTranslation from "../ReusableComponents/CommonRoomTypes.tsx";

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
            footerContent={<p></p>}
        >
            <div className="common-room-show-container">
                {loading ? (
                    <p>Ładowanie...</p>
                ) : commonRooms.length === 0 ? (
                    "Nie znaleziono żadnych pokoi"
                ) : (
                    commonRooms.map((commonRoom) => (
                        <div
                            key={commonRoom.id}
                            className="common-room-card"
                            onClick={() => navigate(`/common-room/${commonRoom.id}`)}
                            style={{ cursor: "pointer" }}
                        >
                            <h2>Typ pokoju: {getRoomStatusTranslation(commonRoom.type)}</h2>
                            <p>Piętro: {commonRoom.floor}</p>
                        </div>
                    ))
                )}
            </div>
        </Template>
    );
}

export default CommonRoomShow;