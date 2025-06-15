import Template from '../Template/Template';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from "../Context/UserContext.tsx";

function EventsCreate() {
    const userContext = useContext(UserContext);
    const organizerId = userContext?.user?.id;

    const [eventName, setEventName] = useState('');
    const [eventDescription, setEventDescription] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [location, setLocation] = useState('');
    const [eventType, setEventType] = useState('');
    const [availableSeats, setAvailableSeats] = useState<number | ''>('');
    const [imageUrl, setImageUrl] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const token = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))
            ?.split('=')[1];

        if (!token) {
            setError('Brak tokena uwierzytelniającego');
            return;
        }

        if (!organizerId) {
            setError('Nieprawidłowy token użytkownika');
            return;
        }

        const newEvent = {
            eventName: eventName,
            description: eventDescription,
            startDateTime: startDate,
            endDateTime: endDate,
            location: location,
            eventType: eventType,
            maxParticipants: availableSeats !== '' ? Number(availableSeats) : null,
            imageUrl: imageUrl || null,
            organizerId: organizerId,
            participantId: []
        };

        try {
            const response = await fetch('/api/event/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(newEvent),
                credentials: 'include'
            });

            if (!response.ok) {
                const errorData = await response.json();

                if (errorData.errors) {
                    const cleanedErrors = errorData.errors.map((err: string) => {
                        const colonIndex = err.indexOf(':');
                        return colonIndex !== -1 ? err.slice(colonIndex + 1).trim() : err;
                    });

                    setError(cleanedErrors.join('\n'));
                } else {
                    setError('Nie udało się utworzyć wydarzenia');
                }
                return;
            }

            setSuccessMessage('Wydarzenie utworzone pomyślnie!');
            setError(null);

            window.scrollTo({ top: 0, behavior: 'smooth' });

            setTimeout(() => {
                navigate('/events');
            }, 1000);

            setEventName('');
            setEventDescription('');
            setStartDate('');
            setEndDate('');
            setLocation('');
            setEventType('');
            setAvailableSeats('');
            setImageUrl('');
        } catch (error: any) {
            setError(error.message || 'Błąd podczas tworzenia wydarzenia');
            window.scrollTo({ top: 0, behavior: 'smooth' });
            setSuccessMessage(null);
        }
    };

    return (
        <Template
            buttons={[
                {text: 'Chat', link: '/chat'},
                {text: 'Wydarzenia', link: '/events'},
                {text: 'Pokoje wspólne', link: '/common-rooms'},
                {text: 'Pokój', link: '/rooms/myInfo'},
                {text: 'Zgłoś problem', link: '/problems'}
            ]}
        >
            <div className="flex flex-col md:flex-row w-full min-h-screen">
                {/* Lewa kolumna */}
                <div className="w-full md:w-1/4 flex justify-center items-start p-5">
                    <button
                        type="button"
                        className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                        onClick={() => navigate(-1)}
                    >
                        ← Powrót
                    </button>
                </div>

                {/* Środkowa kolumna */}
                <div className="w-full md:w-2/4 flex justify-center items-start p-5">
                    <div className="w-full max-w-lg bg-gray-100 p-5 rounded-lg shadow-md">
                        <h2 className="text-2xl font-semibold text-gray-600 text-center mb-4">Stwórz wydarzenie</h2>

                        {error && (
                            <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4 whitespace-pre-line">
                                {error}
                            </div>
                        )}
                        {successMessage && <p className="bg-green-100 text-gray-600 p-3 rounded-lg mb-4">{successMessage}</p>}

                        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                            <div>
                                <label htmlFor="eventName" className="block text-gray-700 mb-1">Nazwa wydarzenia</label>
                                <input
                                    type="text"
                                    id="eventName"
                                    value={eventName}
                                    onChange={(e) => setEventName(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="eventDescription" className="block text-gray-700 mb-1">Opis</label>
                                <textarea
                                    id="eventDescription"
                                    value={eventDescription}
                                    onChange={(e) => setEventDescription(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="startDate" className="block text-gray-700 mb-1">Data rozpoczęcia</label>
                                <input
                                    type="datetime-local"
                                    id="startDate"
                                    value={startDate}
                                    onChange={(e) => setStartDate(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="endDate" className="block text-gray-700 mb-1">Data zakończenia</label>
                                <input
                                    type="datetime-local"
                                    id="endDate"
                                    value={endDate}
                                    onChange={(e) => setEndDate(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="location" className="block text-gray-700 mb-1">Lokalizacja</label>
                                <input
                                    type="text"
                                    id="location"
                                    value={location}
                                    onChange={(e) => setLocation(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="eventType" className="block text-gray-700 mb-1">Typ wydarzenia</label>
                                <select
                                    id="eventType"
                                    value={eventType}
                                    onChange={(e) => setEventType(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                >
                                    <option value="">Wybierz typ</option>
                                    <option value="party">Impreza</option>
                                    <option value="meeting">Spotkanie</option>
                                    <option value="workshop">Warsztat</option>
                                </select>
                            </div>
                            <div>
                                <label htmlFor="availableSeats" className="block text-gray-700 mb-1">Liczba uczestników</label>
                                <input
                                    type="number"
                                    id="availableSeats"
                                    value={availableSeats}
                                    onChange={(e) => setAvailableSeats(Number(e.target.value))}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="imageUrl" className="block text-gray-700 mb-1">URL obrazka</label>
                                <input
                                    type="text"
                                    id="imageUrl"
                                    value={imageUrl}
                                    onChange={(e) => setImageUrl(e.target.value)}
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <button type="submit" className="w-full bg-gray-600 text-white py-2 rounded-lg hover:bg-gray-700 transition">Utwórz wydarzenie</button>
                        </form>
                    </div>
                </div>

                {/* Prawa kolumna */}
                <div className="w-full md:w-1/4"></div>
            </div>
        </Template>
    );
}

export default EventsCreate;