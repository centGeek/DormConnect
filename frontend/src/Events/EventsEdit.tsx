import Template from '../Template/Template';
import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './EventsCreate.css';
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

    const isAdminOrManager = user?.roles.includes('ADMIN') || user?.roles.includes('MANAGER');

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
            } catch (error: any) {
                setError(error.message || 'Error fetching event data');
            }
        };

        fetchEvent();
    }, [eventId]);

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

        if (!isAdminOrManager) {
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
            eventName: eventData.eventName,
            description: eventData.eventDescription,
            startDateTime: new Date(eventData.startDateTime).toISOString(),
            endDateTime: new Date(eventData.endDateTime).toISOString(),
            location: eventData.location,
            eventType: eventData.eventType,
            maxParticipants: eventData.availableSeats !== '' ? Number(eventData.availableSeats) : 1,
            imageUrl: eventData.imageUrl || 'placeholder.jpg',
            organizerId: user.id,
            isApproved: false,
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

            setTimeout(() => navigate('/events'), 1000);
        } catch (error: any) {
            setError(error.message || 'Error updating event');
            setSuccessMessage(null);
        }
    };

    return (
        <Template buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]} footerContent={<p></p>}>
            <div className="events-create-wrapper">
                {/* Lewa kolumna */}
                <div className="left-column">
                    <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={() => navigate('/events')}
                    >
                        ← Back to Events
                    </button>
                </div>

                {/* Środkowa kolumna */}
                <div className="center-column">
                    <div className="events-create-container">
                        <h2>Edit Event</h2>

                        {error && <div className="error-message">{error}</div>}
                        {successMessage && <p className="success-message">{successMessage}</p>}

                        {!isAdminOrManager ? (
                            <div className="error-message">
                                You don't have permission to edit events.
                            </div>
                        ) : (
                            <form onSubmit={handleSubmit} className="event-form">
                                <div className="form-group">
                                    <label htmlFor="eventName">Event's Name</label>
                                    <input
                                        type="text"
                                        id="eventName"
                                        value={eventData.eventName}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter event's name"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="eventDescription">Description</label>
                                    <textarea
                                        id="eventDescription"
                                        value={eventData.eventDescription}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter event description"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="startDateTime">Start Date</label>
                                    <input
                                        type="datetime-local"
                                        id="startDateTime"
                                        value={eventData.startDateTime}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="endDateTime">End Date</label>
                                    <input
                                        type="datetime-local"
                                        id="endDateTime"
                                        value={eventData.endDateTime}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="location">Location</label>
                                    <input
                                        type="text"
                                        id="location"
                                        value={eventData.location}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter event location"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="eventType">Type</label>
                                    <select
                                        id="eventType"
                                        value={eventData.eventType}
                                        onChange={handleChange}
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
                                        value={eventData.availableSeats}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter number of available participants"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="imageUrl">Image URL</label>
                                    <input
                                        type="text"
                                        id="imageUrl"
                                        value={eventData.imageUrl}
                                        onChange={handleChange}
                                        placeholder="Enter image URL (optional)"
                                    />
                                </div>

                                <button type="submit" className="btn btn-primary">Update Event</button>
                            </form>
                        )}
                    </div>
                </div>

                {/* Prawa kolumna */}
                <div className="right-column"></div>
            </div>
        </Template>
    );
}

export default EventsEdit;
