import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './EventsCard.css';

interface Event {
    eventId: number;
    eventName: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
    location: string;
    eventType: string;
    maxParticipants: number;
    participantId: number[];
    imageUrl?: string;
}

interface EventCardProps {
    event: Event;
    userId: number | undefined;
    isOrganizer: boolean;
    onEventDeleted: (eventId: number) => void;  // Callback do usunięcia wydarzenia
}

const EventCard: React.FC<EventCardProps> = ({ event, userId, isOrganizer, onEventDeleted }) => {
    const navigate = useNavigate();
    const [availableSpots, setAvailableSpots] = useState<number>(event.maxParticipants - event.participantId.length);
    const [participants, setParticipants] = useState<number[]>(event.participantId);
    const [loading, setLoading] = useState<boolean>(false);
    const [showConfirm, setShowConfirm] = useState<boolean>(false);

    useEffect(() => {
        setAvailableSpots(event.maxParticipants - participants.length);
    }, [participants, event.maxParticipants]);

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    const handleJoinEvent = async () => {
        if (!userId || participants.includes(userId) || availableSpots <= 0 || !token) return;

        try {
            setLoading(true);

            const response = await fetch(`/api/event/participant/${event.eventId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
            });

            if (!response.ok) throw new Error();

            setParticipants(prev => [...prev, userId]);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleLeaveEvent = async () => {
        if (!userId || !participants.includes(userId) || !token) return;

        try {
            setLoading(true);

            const response = await fetch(`/api/event/participant/${event.eventId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
            });

            if (!response.ok) throw new Error();

            setParticipants(prev => prev.filter(id => id !== userId));
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleEditEvent = () => {
        navigate(`/events/edit/${event.eventId}`);
    };

    const handleDeleteEvent = async () => {
        if (!token) return;

        try {
            setLoading(true);

            const response = await fetch(`/api/event/${event.eventId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
            });

            if (!response.ok) throw new Error();

            // Wywołaj callback po usunięciu
            onEventDeleted(event.eventId);

        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
            setShowConfirm(false);
        }
    };

    return (
        <div className="event-card">
            {event.imageUrl && (
                <img src={event.imageUrl} alt={event.eventName} className="event-image" />
            )}

            <h3>{event.eventName}</h3>
            <p><strong>Opis:</strong> {event.description}</p>
            <p><strong>Data:</strong> {new Date(event.startDateTime).toLocaleString()} - {new Date(event.endDateTime).toLocaleString()}</p>
            <p><strong>Lokalizacja:</strong> {event.location}</p>
            <p><strong>Typ wydarzenia:</strong> {event.eventType}</p>
            <p><strong>Dostępne miejsca:</strong> {availableSpots}</p>

            {isOrganizer ? (
                <div className="organizer-buttons">
                    <button className="btn edit-button" onClick={handleEditEvent}>
                        Edytuj
                    </button>

                    <div className="delete-wrapper">
                        {showConfirm ? (
                            <div className="confirm-popup">
                                <p>Czy na pewno?</p>
                                <div className="confirm-buttons">
                                    <button className="btn confirm-delete" onClick={handleDeleteEvent} disabled={loading}>
                                        {loading ? 'Usuwanie...' : 'Tak'}
                                    </button>
                                    <button className="btn cancel-delete" onClick={() => setShowConfirm(false)}>
                                        Nie
                                    </button>
                                </div>
                            </div>
                        ) : (
                            <button className="btn delete-button" onClick={() => setShowConfirm(true)}>
                                Usuń
                            </button>
                        )}
                    </div>
                </div>
            ) : (
                userId && (
                    participants.includes(userId) ? (
                        <button className="btn leave-button" onClick={handleLeaveEvent} disabled={loading}>
                            {loading ? 'Wychodzenie...' : 'Opuść wydarzenie'}
                        </button>
                    ) : (
                        <button className="btn join-button" onClick={handleJoinEvent} disabled={loading || availableSpots <= 0}>
                            {loading ? 'Dołączanie...' : 'Dołącz do wydarzenia'}
                        </button>
                    )
                )
            )}
        </div>
    );
};

export default EventCard;
