import { useEffect, useState } from "react";
import Template from "../Template/Template.tsx";

interface CommonRoom {
    id: number;
    type: string; // Zmieniono z commonRoomType na type
    floor: number;
}

function CommonRoomShow() {
    const [loading, setLoading] = useState<boolean>(true);
    const [commonRooms, setCommonRooms] = useState<CommonRoom[]>([]);

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
            setCommonRooms(data || []); // Przypisanie danych bezpośrednio
        } catch (error: any) {
            console.error("Error fetching common rooms:", error);
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
                    "No common rooms found"
                ) : (
                    commonRooms.map((commonRoom) => (
                        <div key={commonRoom.id} className="common-room">
                            <h2>Common Room Type: {commonRoom.type}</h2>
                            <p>Floor: {commonRoom.floor}</p>
                        </div>
                    ))
                )}
            </div>
        </Template>
    );
}

export default CommonRoomShow;