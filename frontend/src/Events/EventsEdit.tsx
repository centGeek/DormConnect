import Template from '../Template/Template';
import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { UserContext } from '../Context/UserContext.tsx';

function EventsEdit() {
    const { user } = useContext(UserContext);
    const [eventData, setEventData] = useState({
        eventName: '',
        eventDescription: '',
        startDateTime: '',
        endDateTime: '',
        location: '',
        eventType: '',
        availableSeats: '',
        imageUrl: '',
    });
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { eventId } = useParams();
    const navigate = useNavigate();
    const [isCreator, setIsCreator] = useState(false);

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const token = document.cookie
                    .split('; ')
                    .find(row => row.startsWith('token='))?.split('=')[1];

                if (!token || !eventId) {
                    setError('Brak ID wydarzenia lub tokena');
                    return;
                }

                const response = await fetch(`/api/event/${eventId}`, {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${token}` },
                });

                if (!response.ok) {
                    setError('Nie udało się pobrać wydarzenia');
                    return;
                }

                const data = await response.json();
                setEventData({
                    eventName: data.eventName,
                    eventDescription: data.description,
                    startDateTime: formatDateTimeLocal(data.startDateTime),
                    endDateTime: formatDateTimeLocal(data.endDateTime),
                    location: data.location,
                    eventType: data.eventType,
                    availableSeats: data.maxParticipants.toString(),
                    imageUrl: data.imageUrl || '',
                });

                setIsCreator(user?.id === data.organizerId);
            } catch (error: any) {
                setError(error.message || 'Błąd podczas pobierania danych wydarzenia');
            }
        };

        fetchEvent();
    }, [eventId, user?.id]);

    const formatDateTimeLocal = (dateTimeString: string) => {
        if (!dateTimeString) return '';
        const date = new Date(dateTimeString);
        return date.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { id, value } = e.target;
        setEventData((prevData) => ({ ...prevData, [id]: value }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!user) {
            setError('Nie jesteś zalogowany');
            return;
        }

        if (!isCreator) {
            setError('Nie masz uprawnień do edycji tego wydarzenia.');
            return;
        }

        const token = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))?.split('=')[1];

        if (!token) {
            setError('Brak tokena');
            return;
        }

        const updatedEvent = {
            eventId: Number(eventId),
            eventName: eventData.eventName,
            description: eventData.eventDescription,
            startDateTime: new Date(eventData.startDateTime).toISOString(),
            endDateTime: new Date(eventData.endDateTime).toISOString(),
            location: eventData.location,
            eventType: eventData.eventType,
            maxParticipants: eventData.availableSeats !== '' ? Number(eventData.availableSeats) : 1,
            imageUrl: eventData.imageUrl || 'placeholder.jpg',
            organizerId: user.id,
            approvalStatus: 'WAITING',
            participantId: [],
        };

        try {
            const response = await fetch(`/api/event/${eventId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(updatedEvent),
                credentials: 'include',
            });

            if (!response.ok) {
                const errorData = await response.json();
                setError(errorData.errors ? errorData.errors.join('\n') : 'Nie udało się zaktualizować wydarzenia');
                return;
            }

            setSuccessMessage('Wydarzenie zaktualizowane pomyślnie!');
            setError(null);

            window.scrollTo({ top: 0, behavior: 'smooth' });

            setTimeout(() => navigate('/events'), 1000);
        } catch (error: any) {
            setError(error.message || 'Błąd podczas aktualizacji wydarzenia');
            window.scrollTo({ top: 0, behavior: 'smooth' });
            setSuccessMessage(null);
        }
    };

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
            <div className="flex flex-col items-center w-full min-h-screen p-4 bg-gray-100">
                <div className="w-full max-w-2xl bg-white p-6 md:p-8 rounded-lg shadow-md">
                    <button
                        type="button"
                        className="mb-4 bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                        onClick={() => navigate('/events')}
                    >
                        ← Powrót do wydarzeń
                    </button>

                    <h2 className="text-2xl font-semibold text-gray-700 mb-6 text-center">Edytuj wydarzenie</h2>

                    {error && <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4 whitespace-pre-line">{error}</div>}
                    {successMessage && <p className="bg-green-100 text-green-600 p-3 rounded-lg mb-4">{successMessage}</p>}

                    {!isCreator ? (
                        <div className="bg-red-100 text-red-600 p-3 rounded-lg text-center">
                            Nie masz uprawnień do edycji tego wydarzenia.
                        </div>
                    ) : (
                        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                            <div>
                                <label htmlFor="eventName" className="block text-gray-700 mb-1">Nazwa wydarzenia</label>
                                <input
                                    type="text"
                                    id="eventName"
                                    value={eventData.eventName}
                                    onChange={handleChange}
                                    required
                                    placeholder="Wpisz nazwę wydarzenia"
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="eventDescription" className="block text-gray-700 mb-1">Opis</label>
                                <textarea
                                    id="eventDescription"
                                    value={eventData.eventDescription}
                                    onChange={handleChange}
                                    required
                                    placeholder="Opisz wydarzenie"
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="startDateTime" className="block text-gray-700 mb-1">Data rozpoczęcia</label>
                                <input
                                    type="datetime-local"
                                    id="startDateTime"
                                    value={eventData.startDateTime}
                                    onChange={handleChange}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="endDateTime" className="block text-gray-700 mb-1">Data zakończenia</label>
                                <input
                                    type="datetime-local"
                                    id="endDateTime"
                                    value={eventData.endDateTime}
                                    onChange={handleChange}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="location" className="block text-gray-700 mb-1">Lokalizacja</label>
                                <input
                                    type="text"
                                    id="location"
                                    value={eventData.location}
                                    onChange={handleChange}
                                    required
                                    placeholder="Podaj lokalizację"
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="eventType" className="block text-gray-700 mb-1">Typ wydarzenia</label>
                                <select
                                    id="eventType"
                                    value={eventData.eventType}
                                    onChange={handleChange}
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
                                    value={eventData.availableSeats}
                                    onChange={handleChange}
                                    required
                                    placeholder="Podaj maksymalną liczbę uczestników"
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="imageUrl" className="block text-gray-700 mb-1">URL obrazka</label>
                                <input
                                    type="text"
                                    id="imageUrl"
                                    value={eventData.imageUrl}
                                    onChange={handleChange}
                                    placeholder="Opcjonalnie: link do obrazka"
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition">
                                Zaktualizuj wydarzenie
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </Template>
    );
}

export default EventsEdit;