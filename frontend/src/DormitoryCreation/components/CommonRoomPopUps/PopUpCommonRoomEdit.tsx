import React from 'react';
import CommonRoomTypes from "../../../../../../../Projekt_temp/DormConnect/frontend/src/ReusableComponents/CommonRoomTypes.tsx";

interface PopupFormProps {
    onClose: () => void;
    common_room_id: number;
}

interface CommonRoom {
    id: number;
    type: string;
    floor: number;
    capacity: number;
    timesAWeekYouCanUseIt: number;
    isArchived: boolean;
}

function PopUpCommonRoomEdit({ onClose, common_room_id }: PopupFormProps) {
    const [commonRoom, setCommonRoom] = React.useState<CommonRoom | null>(null);

    const getInformation = async (common_room_id: number) => {
        try {
            const response = await fetch(`/api/common-room/get/${common_room_id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
            });
            if (!response.ok) {
                throw new Error("Failed to fetch common room details");
            }
            const data = await response.json();
            setCommonRoom(data);
        } catch (error) {
            console.error("Error fetching common room details:", error);
        }
    };

    const handleDelete = async () => {
        try {
            const response = await fetch(`/api/common-room/delete/${common_room_id}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
            });
            if (!response.ok) {
                throw new Error("Failed to delete common room");
            }
            alert("Pokój wspólny został usunięty!");
            onClose();
        } catch (error) {
            console.error("Error deleting common room:", error);
            alert("Wystąpił błąd podczas usuwania pokoju wspólnego.");
        }
    };

    React.useEffect(() => {
        getInformation(common_room_id);
    }, [common_room_id]);

    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Szczegóły pokoju wspólnego</h1>
                {commonRoom ? (
                    <ul className="mb-4 text-gray-600">
                        <li><strong>Typ:</strong> {CommonRoomTypes(commonRoom.type)}</li>
                        <li><strong>Piętro:</strong> {commonRoom.floor}</li>
                        <li><strong>Pojemność:</strong> {commonRoom.capacity}</li>
                        <li><strong>Ile razy w tygodniu można użyć:</strong> {commonRoom.timesAWeekYouCanUseIt}</li>
                    </ul>
                ) : (
                    <p className="text-gray-500">Ładowanie danych...</p>
                )}
                <div className="flex justify-between">
                    <button
                        onClick={handleDelete}
                        className="bg-red-500 text-white border border-red-500 px-4 py-2 rounded hover:bg-white hover:text-red-600 transition"
                    >
                        Usuń
                    </button>
                    <button
                        onClick={onClose}
                        className="bg-white text-gray-600 border border-gray-500 px-4 py-2 rounded hover:bg-gray-500 hover:text-white transition"
                    >
                        Zamknij
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PopUpCommonRoomEdit;