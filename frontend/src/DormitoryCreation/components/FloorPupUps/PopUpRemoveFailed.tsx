interface props {
    onClose: () => void;
}

function PopUpRemoveFailed({ onClose }: props) {
    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 justify-center rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700 text-center">Kreator</h1>
                <p className="text-gray-600 mb-6 text-center">
                    Coś poszło nie tak i nie udało się usunąć piętra.
                </p>
                <p className="text-gray-600 mb-6 text-center text-">
                    Pamiętaj, że możesz usunąć tylko ostatnie piętro.
                </p>
                <div className="flex justify-center">
                    <button
                        onClick={onClose}
                        className="bg-red-500 text-white border border-red-500 px-4 py-2 rounded hover:bg-white hover:text-red-600 transition"
                    >
                        Zamknij
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PopUpRemoveFailed;