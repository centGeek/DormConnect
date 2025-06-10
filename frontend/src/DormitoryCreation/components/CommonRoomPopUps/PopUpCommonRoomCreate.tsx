import React from 'react';
import Cookies from 'js-cookie';

interface PopupFormProps {
    floor: number;
    onClose: () => void;
    onSucced: () => void;
}

function PopUpCommonRoomCreate({ onClose, floor, onSucced }: PopupFormProps) {
    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const formElement = event.currentTarget;
        const formData = new FormData(formElement);

        const newCommonRoom = {
            type: formData.get('typeRoom'),
            capacity: formData.get('capacity'),
            floor: floor,
            maxTimeYouCanStay: formData.get('maxTimeYouCanStay'),
            howManyTimesAWeekYouCanUseIt: formData.get('howManyTimesAWeekYouCanUseIt'),
        };

        try {
            const response = await fetch('api/common-room/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('token')}`,
                },
                body: JSON.stringify(newCommonRoom),
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Failed to create common room');
            }
            onClose();
            onSucced();
        } catch (error) {
            console.error('Error creating common room:', error);
            alert('Wystąpił błąd podczas dodawania pokoju wspólnego.');
        }
    };

    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50">
            <div className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg w-96">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Dodaj pokój wspólny</h1>
                <form onSubmit={handleSubmit}>
                    <label className="block mb-2 text-gray-600">Wybierz typ pokoju</label>
                    <select name = "typeRoom" className="w-full border border-gray-300 rounded px-3 py-2 mb-4" required>
                        <option value="">Wybierz...</option>
                        <option value="STUDY_ROOM">Pokój cichej nauki</option>
                        <option value="GYM">Siłownia</option>
                        <option value="LAUNDRY">Pralnia</option>
                        <option value="BILLARD_ROOM">Pokój bilardowy</option>
                        <option value="TV_ROOM">Pokój telewizyjny</option>
                        <option value="FITNESS_ROOM">Sala Fitness</option>
                        <option value="TABLE_TENNIS_ROOM">Tenis Stołowy</option>
                    </select>
                    <label className="block mb-2 text-gray-600">Ile będą wynosiły okienka czasowe?</label>
                    <input
                        type="number"
                        name = "maxTimeYouCanStay"
                        className="w-full border border-gray-300 rounded px-3 py-2 mb-4"
                        placeholder="Okienka czasowe (1-4)"
                        min={1}
                        max={4}
                        required
                    />
                    <label>Ile razy w tygodniu można zarezerwować sale?</label>
                    <input
                        type="number"
                        name="howManyTimesAWeekYouCanUseIt"
                        className="w-full border border-gray-300 rounded px-3 py-2 mb-4"
                        min={1}
                        max={10}
                        placeholder="Ile razy można skorzystać (1-10)"
                        required
                    />
                    <label>Ile osób na raz może tam przebywać?</label>
                    <input
                        type="number"
                        name="capacity"
                        className="w-full border border-gray-300 rounded px-3 py-2 mb-4"
                        min={1}
                        max={40}
                        placeholder="Minimum 1"
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

export default PopUpCommonRoomCreate;