import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

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
        <div className="event-card p-5 bg-white border border-gray-400 rounded-lg shadow-md flex flex-col items-center gap-4 transition-transform duration-300 relative z-10 hover:translate-y-[-8px]">
            {event.imageUrl && (
                <img
                    src={event.imageUrl}
                    alt={event.eventName}
                    className="event-image w-full h-52 object-contain rounded-lg mb-4 bg-transparent"
                />
            )}

            <h3 className="text-2xl font-semibold text-gray-500 m-0">{event.eventName}</h3>
            <p className="text-base text-gray-800 m-0 text-center"><strong>Opis:</strong> {event.description}</p>
            <p className="text-base text-gray-800 m-0 text-center"><strong>Data:</strong> {formatDate(event.startDateTime)} - {formatDate(event.endDateTime)}</p>
            <p className="text-base text-gray-800 m-0 text-center"><strong>Lokalizacja:</strong> {event.location}</p>
            <p className="text-base text-gray-800 m-0 text-center"><strong>Typ wydarzenia:</strong> {event.eventType}</p>
            <p className="text-base text-gray-800 m-0 text-center"><strong>Dostępne miejsca:</strong> {availableSpots}</p>

            {isOrganizer ? (
                <div className="organizer-buttons flex justify-center gap-3 w-full mt-3 flex-wrap">
                    <button
                        className="edit-button flex-1 min-w-[120px] py-2 bg-blue-400 text-white rounded-lg font-medium text-center transition-transform duration-200 hover:bg-blue-500 hover:scale-105"
                        onClick={handleEditEvent}
                    >
                        Edytuj
                    </button>

                    <div className="delete-wrapper relative flex-1 min-w-[120px] z-10">
                        <button
                            className="delete-button w-full py-2 bg-red-500 text-white rounded-lg font-medium text-center transition-transform duration-200 hover:bg-red-700 hover:scale-105"
                            ref={deleteButtonRef}
                            onClick={() => setShowConfirm(prev => !prev)}
                        >
                            Usuń
                        </button>

                        {showConfirm && (
                            <div
                                className="confirm-popup absolute top-10 left-1/2 transform -translate-x-1/2 bg-white border border-gray-300 p-3 rounded-lg shadow-lg z-50 w-[250px] text-center"
                                ref={confirmPopupRef}
                            >
                                <p>Czy na pewno?</p>
                                <div className="confirm-buttons mt-2 flex gap-2 justify-center">
                                    <button
                                        className="confirm-delete flex-1 py-2 bg-red-500 text-white rounded-md font-medium hover:bg-red-700"
                                        onClick={handleDeleteEvent}
                                        disabled={loading}
                                    >
                                        {loading ? 'Usuwanie...' : 'Tak'}
                                    </button>
                                    <button
                                        className="cancel-delete flex-1 py-2 bg-gray-500 text-white rounded-md font-medium hover:bg-gray-700"
                                        onClick={() => setShowConfirm(false)}
                                    >
                                        Nie
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            ) : (
                userId && (
                    participants.includes(userId) ? (
                        <button
                            className="leave-button py-2 px-4 bg-red-500 text-white rounded-lg font-medium transition duration-300 hover:bg-red-700"
                            onClick={handleLeaveEvent}
                            disabled={loading}
                        >
                            {loading ? 'Wychodzenie...' : 'Opuść wydarzenie'}
                        </button>
                    ) : (
                        <button
                            className="join-button py-2 px-4 bg-green-500 text-white rounded-lg font-medium transition duration-300 hover:bg-green-600"
                            onClick={handleJoinEvent}
                            disabled={loading || availableSpots <= 0}
                        >
                            {loading ? 'Dołączanie...' : 'Dołącz do wydarzenia'}
                        </button>
                    )
                )
            )}
        </div>
    );
};

export default EventCard;