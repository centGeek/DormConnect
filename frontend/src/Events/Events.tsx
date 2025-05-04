import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import Template from '../Template/Template.tsx';
import EventsSection from './EventsSection';
import './Events.css';

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
    availableSpots: number;
}

function Events() {
    const { state } = useLocation();
    const successMessage = state?.successMessage;

    const [events, setEvents] = useState<Event[]>([]);
    const [organizedEvents, setOrganizedEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const navigate = useNavigate();

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    const user = token ? parseJwt(token) : null;
    const userId = user?.id;

    const fetchEvents = async () => {
        try {
            setLoading(true);

            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event?page=0&size=20&sort=startDateTime,asc`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się pobrać wydarzeń');
            }

            const data = await response.json();
            setEvents(data.content || []);
        } catch (error: any) {
            console.error('Błąd podczas pobierania wydarzeń:', error);
            setError(error.message || 'Wystąpił błąd podczas pobierania wydarzeń.');
        } finally {
            setLoading(false);
        }
    };

    const fetchOrganizedEvents = async () => {
        try {
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event/organizer`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się pobrać organizowanych wydarzeń');
            }

            const data = await response.json();
            setOrganizedEvents(data.content || []);
        } catch (error: any) {
            console.error('Błąd podczas pobierania organizowanych wydarzeń:', error);
        }
    };

    const handleAddEvent = () => {
        navigate('/events/create');
    };

    const joinEvent = async (eventId: number) => {
        try {
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event/participant/${eventId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się dołączyć do wydarzenia');
            }

            setEvents(events.map(event => {
                if (event.eventId === eventId) {
                    return {
                        ...event,
                        participantId: [...event.participantId, userId!],  // Dodaj użytkownika
                    };
                }
                return event;
            }));
        } catch (error: any) {
            console.error('Błąd podczas dołączania do wydarzenia:', error);
        }
    };

    const leaveEvent = async (eventId: number) => {
        try {
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event/participant/${eventId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się opuścić wydarzenia');
            }

            setEvents(events.map(event => {
                if (event.eventId === eventId) {
                    return {
                        ...event,
                        participantId: event.participantId.filter(id => id !== userId),  // Usuń użytkownika
                    };
                }
                return event;
            }));
        } catch (error: any) {
            console.error('Błąd podczas opuszczania wydarzenia:', error);
        }
    };

    useEffect(() => {
        fetchEvents();
        fetchOrganizedEvents();
    }, []);

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="events-container">
                {successMessage && <div className="success-message">{successMessage}</div>}

                <h2>All Events</h2>
                <button className="btn btn-primary add-event-button" onClick={handleAddEvent}>
                    Add Event
                </button>

                {loading && <p>Ładowanie wydarzeń...</p>}
                {error && <p className="error-message">{error}</p>}

                <EventsSection
                    title="Wydarzenia organizowane przez Ciebie"
                    events={organizedEvents}
                    joinEvent={joinEvent}
                    leaveEvent={leaveEvent}
                    userId={userId}
                    pageSize={4}
                />

                <EventsSection
                    title="Wydarzenia, w których bierzesz udział"
                    events={events}
                    joinEvent={joinEvent}
                    leaveEvent={leaveEvent}
                    userId={userId}
                    pageSize={4}
                />
            </div>
        </Template>
    );
}

export default Events;
