import React, { useState } from 'react';
import axios from 'axios';
import Cookies from "js-cookie";

interface PopupFormProps {
    floor: number;
    onClose: () => void;
    onSucced: () => void;
}

function PopUpRoomCreate({ onClose, floor, onSucced }: PopupFormProps) {
    const [number, setNumber] = useState('');
    const [capacity, setCapacity] = useState(2);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const newRoom = {
            number: `${floor}-${number}`,
            capacity,
            floor,
        };

        try {
            await axios.post('/api/dorm/room/create', newRoom, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('token')}`,
                },
                withCredentials: true,
            });
            onClose();
            onSucced();
        } catch (error) {
            console.error('Błąd podczas tworzenia pokoju:', error);
        }
    };

    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Dodaj pokój</h1>
                <form onSubmit={handleSubmit}>
                    <label className="block mb-2 text-gray-600">Numer pokoju</label>
                    <input
                        type="text"
                        value={number}
                        onChange={(e) => setNumber(e.target.value)}
                        className="w-full border border-gray-300 rounded px-3 py-2 mb-4"
                        placeholder="Numer pokoju"
                        required
                    />
                    <label className="block mb-2 text-gray-600">Pojemność</label>
                    <input
                        type="number"
                        value={capacity}
                        onChange={(e) => setCapacity(parseInt(e.target.value))}
                        className="w-full border border-gray-300 rounded px-3 py-2 mb-4"
                        placeholder="Pojemność"
                        min={1}
                        required
                    />
                    <div className="flex justify-between">
                        <button
                            type="submit"
                            className="bg-gray-500 text-white border border-gray-500 px-4 py-2 rounded hover:bg-white hover:text-gray-600 transition"
                        >
                            Zapisz
                        </button>
                        <button
                            type="button"
                            onClick={onClose}
                            className="bg-white text-gray-600 border border-gray-500 px-4 py-2 rounded hover:bg-gray-500 hover:text-white transition"
                        >
                            Zamknij
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default PopUpRoomCreate;