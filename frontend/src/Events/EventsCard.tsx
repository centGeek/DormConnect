import React, { useState, useEffect, useRef, useCallback } from 'react';
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
    onEventDeleted: (eventId: number) => void;
}

const EventCard: React.FC<EventCardProps> = ({ event, userId, isOrganizer, onEventDeleted }) => {
    const navigate = useNavigate();
    const [availableSpots, setAvailableSpots] = useState<number>(event.maxParticipants - event.participantId.length);
    const [participants, setParticipants] = useState<number[]>(event.participantId);
    const [loading, setLoading] = useState<boolean>(false);
    const [showConfirm, setShowConfirm] = useState<boolean>(false);
    const confirmPopupRef = useRef<HTMLDivElement>(null);
    const deleteButtonRef = useRef<HTMLButtonElement>(null);

    useEffect(() => {
        setAvailableSpots(event.maxParticipants - participants.length);
    }, [participants, event.maxParticipants]);

    const handleClickOutside = useCallback((event: MouseEvent) => {
        if (
            confirmPopupRef.current &&
            !confirmPopupRef.current.contains(event.target as Node) &&
            deleteButtonRef.current &&
            !deleteButtonRef.current.contains(event.target as Node)
        ) {
            setShowConfirm(false);
        }
    }, []);

    useEffect(() => {
        if (showConfirm) {
            document.addEventListener('mousedown', handleClickOutside);
        } else {
            document.removeEventListener('mousedown', handleClickOutside);
        }

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [showConfirm, handleClickOutside]);

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
            onEventDeleted(event.eventId);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
            setShowConfirm(false);
        }
    };

    const formatDate = (date: string) => {
        return new Date(date).toLocaleString('pl-PL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    };

    return (
        <div className="event-card">
            {event.imageUrl && (
                <img src={event.imageUrl} alt={event.eventName} className="event-image" />
            )}

            <h3>{event.eventName}</h3>
            <p><strong>Opis:</strong> {event.description}</p>
            <p><strong>Data:</strong> {formatDate(event.startDateTime)} - {formatDate(event.endDateTime)}</p>
            <p><strong>Lokalizacja:</strong> {event.location}</p>
            <p><strong>Typ wydarzenia:</strong> {event.eventType}</p>
            <p><strong>Dostępne miejsca:</strong> {availableSpots}</p>

            {isOrganizer ? (
                <div className="organizer-buttons">
                    <button className="btn edit-button" onClick={handleEditEvent}>
                        Edytuj
                    </button>

                    <div className="delete-wrapper">
                        <button
                            className="btn delete-button"
                            ref={deleteButtonRef}
                            onClick={() => setShowConfirm(prev => !prev)}
                        >
                            Usuń
                        </button>

                        <div
                            className="confirm-popup"
                            ref={confirmPopupRef}
                            style={{ display: showConfirm ? 'block' : 'none' }}
                        >
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
