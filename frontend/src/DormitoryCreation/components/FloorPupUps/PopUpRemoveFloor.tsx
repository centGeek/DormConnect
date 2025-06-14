import Cookies from "js-cookie";

interface PopupRemoveFloorProps {
    onClose: () => void,
    floor: number,
    onSucess: () => void,
}

function PopUpRemoveFloor({onClose, floor, onSucess}: PopupRemoveFloorProps) {

    const handleRemoveFloor = async () => {
        console.log(floor);
        try {
            const response = await fetch(`/api/floors/delete-floor/${floor}`, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    "Authorization": `Bearer ${Cookies.get('token')}`,
                }
            });
            if (!response.ok) {
                throw new Error('Failed to remove floor');
            }
            console.log(`Floor ${floor} removed successfully`);
            onClose();
            onSucess();
        } catch (error) {
            console.error('Error removing floor:', error);
        }
    };
    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Usuń piętro</h1>
                <p className="text-gray-600 mb-6">
                    Czy na pewno chcesz usunąć całe piętro? Tej operacji nie można cofnąć.
                </p>
                <div className="flex justify-between">
                    <button
                        onClick={() => handleRemoveFloor()}
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

export default PopUpRemoveFloor;