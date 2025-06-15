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
                    setError('Event ID or token is missing');
                    return;
                }

                const response = await fetch(`/api/event/${eventId}`, {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${token}` },
                });

                if (!response.ok) {
                    setError('Failed to fetch event');
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
                setError(error.message || 'Error fetching event data');
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
            setError('User not logged in');
            return;
        }

        if (!isCreator) {
            setError('You do not have permission to edit events.');
            return;
        }

        const token = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))?.split('=')[1];

        if (!token) {
            setError('No token found');
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
                setError(errorData.errors ? errorData.errors.join('\n') : 'Failed to update event');
                return;
            }

            setSuccessMessage('Event updated successfully!');
            setError(null);

            window.scrollTo({ top: 0, behavior: 'smooth' });

            setTimeout(() => navigate('/events'), 1000);
        } catch (error: any) {
            setError(error.message || 'Error updating event');

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
            <div className="flex flex-col items-center w-full min-h-screen p-5 bg-gray-100">
                <div className="w-full max-w-4xl bg-white p-8 rounded-lg shadow-md">
                    <button
                        type="button"
                        className="mb-4 bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition"
                        onClick={() => navigate('/events')}
                    >
                        ← Back to Events
                    </button>

                    <h2 className="text-2xl font-semibold text-gray-700 mb-6">Edit Event</h2>

                    {error && <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4">{error}</div>}
                    {successMessage && <p className="bg-green-100 text-green-600 p-3 rounded-lg mb-4">{successMessage}</p>}

                    {!isCreator ? (
                        <div className="bg-red-100 text-red-600 p-3 rounded-lg">
                            You don't have permission to edit events.
                        </div>
                    ) : (
                        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                            <div>
                                <label htmlFor="eventName" className="block text-gray-700 mb-1">Event's Name</label>
                                <input
                                    type="text"
                                    id="eventName"
                                    value={eventData.eventName}
                                    onChange={handleChange}
                                    required
                                    placeholder="Enter event's name"
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="eventDescription" className="block text-gray-700 mb-1">Description</label>
                                <textarea
                                    id="eventDescription"
                                    value={eventData.eventDescription}
                                    onChange={handleChange}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="startDateTime" className="block text-gray-700 mb-1">Start Date</label>
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
                                <label htmlFor="endDateTime" className="block text-gray-700 mb-1">End Date</label>
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
                                <label htmlFor="location" className="block text-gray-700 mb-1">Location</label>
                                <input
                                    type="text"
                                    id="location"
                                    value={eventData.location}
                                    onChange={handleChange}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="eventType" className="block text-gray-700 mb-1">Type</label>
                                <select
                                    id="eventType"
                                    value={eventData.eventType}
                                    onChange={handleChange}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                >
                                    <option value="">Choose type</option>
                                    <option value="party">Party</option>
                                    <option value="meeting">Meeting</option>
                                    <option value="workshop">Workshop</option>
                                </select>
                            </div>
                            <div>
                                <label htmlFor="availableSeats" className="block text-gray-700 mb-1">Number of Participants</label>
                                <input
                                    type="number"
                                    id="availableSeats"
                                    value={eventData.availableSeats}
                                    onChange={handleChange}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="imageUrl" className="block text-gray-700 mb-1">Image URL</label>
                                <input
                                    type="text"
                                    id="imageUrl"
                                    value={eventData.imageUrl}
                                    onChange={handleChange}
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition">
                                Update Event
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </Template>
    );
}

export default EventsEdit;