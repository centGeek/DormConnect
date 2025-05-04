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
    const [isOrganizer, setIsOrganizer] = useState<boolean>(false);  // Stan, który będzie informował, czy użytkownik jest organizatorem

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
            setIsOrganizer(data.content.length > 0);  // Sprawdzamy, czy użytkownik jest organizatorem
        } catch (error: any) {
            console.error('Błąd podczas pobierania organizowanych wydarzeń:', error);
        }
    };

    const editEvent = (eventId: number) => {
        navigate(`/events/edit/${eventId}`);
    };

    useEffect(() => {
        fetchEvents();
        fetchOrganizedEvents();
    }, []);

    const handleAddEvent = () => {
        navigate('/events/create');
    };

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

                {isOrganizer && (  // Wyświetlamy sekcję organizowanych wydarzeń tylko, jeśli użytkownik jest organizatorem
                    <EventsSection
                        title="Wydarzenia organizowane przez Ciebie"
                        events={organizedEvents}
                        editEvent={editEvent}
                        userId={userId}
                        pageSize={4}
                        isOrganizerSection={true}
                    />
                )}

                <EventsSection
                    title="Wszystkie wydarzenia."
                    events={events}
                    joinEvent={() => {}}
                    leaveEvent={() => {}}
                    userId={userId}
                    pageSize={4}
                    isOrganizerSection={false}
                />
            </div>
        </Template>
    );
}

export default Events;