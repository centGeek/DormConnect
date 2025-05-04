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
}

const EventCard: React.FC<EventCardProps> = ({ event, userId, isOrganizer }) => {
    const navigate = useNavigate();
    const [availableSpots, setAvailableSpots] = useState<number>(event.maxParticipants - event.participantId.length);
    const [participants, setParticipants] = useState<number[]>(event.participantId);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        setAvailableSpots(event.maxParticipants - participants.length);
    }, [participants, event.maxParticipants]);

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    // Dołącz do wydarzenia
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

            if (!response.ok) throw new Error('Nie udało się dołączyć do wydarzenia.');

            // Jeśli sukces — aktualizuj stan
            setParticipants(prev => [...prev, userId]);
        } catch (error) {
            console.error(error);
            alert('Wystąpił błąd przy dołączaniu do wydarzenia.');
        } finally {
            setLoading(false);
        }
    };

    // Opuść wydarzenie
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

            if (!response.ok) throw new Error('Nie udało się opuścić wydarzenia.');

            // Jeśli sukces — aktualizuj stan
            setParticipants(prev => prev.filter(id => id !== userId));
        } catch (error) {
            console.error(error);
            alert('Wystąpił błąd przy opuszczaniu wydarzenia.');
        } finally {
            setLoading(false);
        }
    };

    // Edytuj wydarzenie
    const handleEditEvent = () => {
        navigate(`/events/edit/${event.eventId}`);
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
                <button className="btn edit-button" onClick={handleEditEvent}>
                    Edytuj
                </button>
            ) : (
                userId && (
                    participants.includes(userId) ? (
                        <button className="btn leave-button" onClick={handleLeaveEvent} disabled={loading}>
                            {loading ? 'Leave...' : 'Leave event'}
                        </button>
                    ) : (
                        <button className="btn join-button" onClick={handleJoinEvent} disabled={loading || availableSpots <= 0}>
                            {loading ? 'Join...' : 'Join event'}
                        </button>
                    )
                )
            )}
        </div>
    );
};

export default EventCard;
