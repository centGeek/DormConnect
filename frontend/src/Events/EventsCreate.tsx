import Template from '../Template/Template';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from "../Context/UserContext.tsx";

function EventsCreate() {
    const userContext = useContext(UserContext);
    const isAdmin = userContext?.user?.roles.includes('ADMIN') || userContext?.user?.roles.includes('MANAGER');
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
            setError('No token found');
            return;
        }

        if (!organizerId) {
            setError('Invalid user token');
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
                    setError('Failed to create event');
                }
                return;
            }

            setSuccessMessage('Event created successfully!');
            setError(null);

            window.scrollTo({ top: 0, behavior: 'smooth' });

            setTimeout(() => {
                navigate('/events');
            }, 1000);

            // Czyszczenie formularza
            setEventName('');
            setEventDescription('');
            setStartDate('');
            setEndDate('');
            setLocation('');
            setEventType('');
            setAvailableSeats('');
            setImageUrl('');
        } catch (error: any) {
            setError(error.message || 'Error creating event');

            window.scrollTo({ top: 0, behavior: 'smooth' });

            setSuccessMessage(null);
        }
    };

    return (
        <Template
            buttons={[]}
            footerContent={<p></p>}
        >
            <div className="flex w-full min-h-screen">
                {/* Lewa kolumna */}
                <div className="flex-1 flex justify-center items-start p-5">
                    <button
                        type="button"
                        className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                        onClick={() => navigate('/events')}
                    >
                        ← Back to Events
                    </button>
                </div>

                {/* Środkowa kolumna */}
                <div className="flex-3 flex justify-center items-start p-5">
                    <div className="w-full max-w-lg bg-gray-100 p-5 rounded-lg shadow-md">
                        <h2 className="text-2xl font-semibold text-gray-600 text-center mb-4">Create Event</h2>

                        {error && (
                            <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4">
                                {error.split('\n').map((err, idx) => (
                                    <p key={idx}>{err}</p>
                                ))}
                            </div>
                        )}
                        {successMessage && <p className="bg-green-100 text-gray-600 p-3 rounded-lg mb-4">{successMessage}</p>}

                        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                            <div>
                                <label htmlFor="eventName" className="block text-gray-700 mb-1">Event's Name</label>
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
                                <label htmlFor="eventDescription" className="block text-gray-700 mb-1">Description</label>
                                <textarea
                                    id="eventDescription"
                                    value={eventDescription}
                                    onChange={(e) => setEventDescription(e.target.value)}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="startDate" className="block text-gray-700 mb-1">Start Date</label>
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
                                <label htmlFor="endDate" className="block text-gray-700 mb-1">End Date</label>
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
                                <label htmlFor="location" className="block text-gray-700 mb-1">Location</label>
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
                                <label htmlFor="eventType" className="block text-gray-700 mb-1">Type</label>
                                <select
                                    id="eventType"
                                    value={eventType}
                                    onChange={(e) => setEventType(e.target.value)}
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
                                    value={availableSeats}
                                    onChange={(e) => setAvailableSeats(Number(e.target.value))}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <div>
                                <label htmlFor="imageUrl" className="block text-gray-700 mb-1">Image URL</label>
                                <input
                                    type="text"
                                    id="imageUrl"
                                    value={imageUrl}
                                    onChange={(e) => setImageUrl(e.target.value)}
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>
                            <button type="submit" className="w-full bg-gray-300 text-white py-2 rounded-lg hover:bg-gray-300 transition">Create Event</button>
                        </form>
                    </div>
                </div>

                {/* Prawa kolumna */}
                <div className="flex-1"></div>
            </div>
        </Template>
    );
}

export default EventsCreate;