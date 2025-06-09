import Cookies from "js-cookie";

interface PopUpRoomDeleteProps {
    onClose: () => void;
    roomNumber: string;
    roomId:number;
}

function PopUpRoomDelete({ onClose, roomNumber, roomId }: PopUpRoomDeleteProps) {
    const handleDelete = async () => {
        // try {
        //     const response = await fetch(`/api/rooms/delete/${roomId}`, {
        //         method: "DELETE",
        //         headers: {
        //             "Content-Type": "application/json",
        //             'Authorization': `Bearer ${Cookies.get('token')}`,
        //         },
        //         credentials: "include",
        //     });
        //
        //     if (!response.ok) {
        //         throw new Error("Nie udało się usunąć pokoju.");
        //     }
        //
        //     alert("Pokój został pomyślnie usunięty!");
        //     onClose();
        // } catch (error) {
        //     console.error("Błąd podczas usuwania pokoju:", error);
        //     alert("Wystąpił błąd podczas usuwania pokoju.");
        // }
        alert("Czekamy, aż maciej zrobi usuwanie pokoi")
    };
    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Usuń pokój</h1>
                <p className="text-gray-600 mb-6">
                    Czy na pewno chcesz usunąć pokój {roomNumber}? Tej operacji nie można cofnąć.
                </p>
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
                        Anuluj
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PopUpRoomDelete;