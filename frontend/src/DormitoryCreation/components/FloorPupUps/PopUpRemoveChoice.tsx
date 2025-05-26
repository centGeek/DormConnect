interface PopupRemoveChoiceProps {
    onClose: () => void;
    onRemoveFloor: () => void;
    onRemoveRooms: () => void;
}

function PopUpRemoveChoice({ onClose, onRemoveFloor, onRemoveRooms }: PopupRemoveChoiceProps) {
    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Usuń piętro lub pokoje</h1>
                <p className="text-gray-600 mb-6">
                    Czy chcesz usunąć całe piętro, czy tylko wszystkie pokoje na piętrze?
                </p>
                <div className="flex justify-between">
                    <button
                        onClick={onRemoveFloor}
                        className="bg-red-500 text-white border border-red-500 px-4 py-2 rounded hover:bg-white hover:text-red-600 transition"
                    >
                        Usuń piętro
                    </button>
                    <button
                        onClick={onRemoveRooms}
                        className="bg-yellow-500 text-white border border-yellow-500 px-4 py-2 rounded hover:bg-white hover:text-yellow-600 transition"
                    >
                        Usuń pokoje
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

export default PopUpRemoveChoice;