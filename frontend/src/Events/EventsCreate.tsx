import Template from '../Template/Template';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import './EventsCreate.css';

function EventsCreate() {
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

        const user = parseJwt(token);
        const organizerId = user?.id;

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
            setSuccessMessage(null);
        }
    };

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="events-create-container">
                <h2>Create Event</h2>

                {error && (
                    <div className="error-message">
                        {error.split('\n').map((err, idx) => (
                            <p key={idx}>{err}</p>
                        ))}
                    </div>
                )}
                {successMessage && <p className="success-message">{successMessage}</p>}

                <form onSubmit={handleSubmit} className="event-form">
                    <div className="form-group">
                        <label htmlFor="eventName">Event's Name</label>
                        <input
                            type="text"
                            id="eventName"
                            value={eventName}
                            onChange={(e) => setEventName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="eventDescription">Description</label>
                        <textarea
                            id="eventDescription"
                            value={eventDescription}
                            onChange={(e) => setEventDescription(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="startDate">Start Date</label>
                        <input
                            type="datetime-local"
                            id="startDate"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="endDate">End Date</label>
                        <input
                            type="datetime-local"
                            id="endDate"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="location">Location</label>
                        <input
                            type="text"
                            id="location"
                            value={location}
                            onChange={(e) => setLocation(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="eventType">Type</label>
                        <select
                            id="eventType"
                            value={eventType}
                            onChange={(e) => setEventType(e.target.value)}
                            required
                        >
                            <option value="">Choose type</option>
                            <option value="party">Party</option>
                            <option value="meeting">Meeting</option>
                            <option value="workshop">Workshop</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="availableSeats">Number of Participants</label>
                        <input
                            type="number"
                            id="availableSeats"
                            value={availableSeats}
                            onChange={(e) => setAvailableSeats(Number(e.target.value))}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="imageUrl">Image URL</label>
                        <input
                            type="text"
                            id="imageUrl"
                            value={imageUrl}
                            onChange={(e) => setImageUrl(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="btn btn-primary">Create Event</button>
                </form>
            </div>
        </Template>
    );
}

export default EventsCreate;