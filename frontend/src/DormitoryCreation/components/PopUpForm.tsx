import React from "react";

interface PopupFormProps {
    onClose: () => void;
}

function PopupForm({ onClose }: PopupFormProps) {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
            <div className="bg-white p-6 rounded shadow-lg w-96">
                <h2 className="text-xl font-bold mb-4">Dodaj Pokój Wspólny</h2>
                <form>
                    <div className="mb-4">
                        <label className="block text-gray-700">Nazwa pokoju:</label>
                        <input
                            type="text"
                            className="w-full border border-gray-300 rounded px-3 py-2"
                        />
                    </div>
                    <div className="flex justify-end">
                        <button
                            type="button"
                            onClick={onClose}
                            className="bg-gray-500 text-white px-4 py-2 rounded mr-2"
                        >
                            Anuluj
                        </button>
                        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
                            Zapisz
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default PopupForm;