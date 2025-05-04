import { useEffect, useState, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import Template from '../Template/Template.tsx';
import { UserContext } from '../Context/UserContext.tsx';
import './Events.css';

// Interfejs Event
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
    imageUrl?: string; // <- Dodane imageUrl
}

function Events() {
    const { state } = useLocation();
    const successMessage = state?.successMessage;

    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const [totalPages, setTotalPages] = useState<number>(1);
    const navigate = useNavigate();

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    const user = token ? parseJwt(token) : null;
    const userId = user?.id;

    const fetchEvents = async (page: number) => {
        try {
            setLoading(true);

            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event?page=${page}&size=5&sort=startDateTime,asc`, {
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
            setTotalPages(data.totalPages);
        } catch (error: any) {
            console.error('Błąd podczas pobierania wydarzeń:', error);
            setError(error.message || 'Wystąpił błąd podczas pobierania wydarzeń.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEvents(page);
    }, [page]);

    const handleAddEvent = () => {
        navigate('/events/create');
    };

    const handlePageChange = (newPage: number) => {
        setPage(newPage);
    };

    const updateEventParticipation = (eventId: number, isJoining: boolean) => {
        setEvents(events.map(event => {
            if (event.eventId === eventId) {
                const updatedParticipants = isJoining
                    ? [...event.participantId, userId!]
                    : event.participantId.filter(id => id !== userId);

                const availableSpots = event.maxParticipants - updatedParticipants.length;

                return {
                    ...event,
                    participantId: updatedParticipants,
                    availableSpots: availableSpots
                };
            }
            return event;
        }));
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

            updateEventParticipation(eventId, true);
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

            updateEventParticipation(eventId, false);
        } catch (error: any) {
            console.error('Błąd podczas opuszczania wydarzenia:', error);
        }
    };
    const userContext = useContext(UserContext);
    const isAdmin = userContext?.user?.roles.includes('ADMIN');
    const handleAdminNavigation = () => {
        navigate('/events/admin/AdminEvents');
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
                {isAdmin && (
                    <button
                        className="btn btn-secondary admin-button"
                        onClick={handleAdminNavigation}
                    >
                        Admin Panel
                    </button>
                )}

                {loading && <p>Ładowanie wydarzeń...</p>}
                {error && <p className="error-message">{error}</p>}

                <div className="events-list">
                    {events.length === 0 ? (
                        <p>Brak dostępnych wydarzeń.</p>
                    ) : (
                        events.map((event) => (
                            <div key={event.eventId} className="event-card">
                                {/* Wyświetlenie obrazka jeśli istnieje */}
                                {event.imageUrl && (
                                    <img src={event.imageUrl} alt={event.eventName} className="event-image" />
                                )}

                                <h3>{event.eventName}</h3>
                                <p>{event.description}</p>
                                <p>
                                    <strong>Data:</strong> {new Date(event.startDateTime).toLocaleString()} -{' '}
                                    {new Date(event.endDateTime).toLocaleString()}
                                </p>
                                <p><strong>Lokalizacja:</strong> {event.location}</p>
                                <p><strong>Typ:</strong> {event.eventType}</p>
                                <p><strong>Dostępne miejsca:</strong> {event.maxParticipants - event.participantId.length}</p>

                                {/* Przyciski Join/Leave */}
                                {userId && (
                                    event.participantId.includes(userId) ? (
                                        <button className="btn leave-button" onClick={() => leaveEvent(event.eventId)}>
                                            Opuść wydarzenie
                                        </button>
                                    ) : (
                                        <button
                                            className="btn join-button"
                                            onClick={() => joinEvent(event.eventId)}
                                            disabled={event.maxParticipants - event.participantId.length <= 0}
                                        >
                                            Dołącz do wydarzenia
                                        </button>
                                    )
                                )}
                            </div>
                        ))
                    )}
                </div>

                {/* Paginacja */}
                <div className="pagination">
                    {Array.from({ length: totalPages }, (_, index) => (
                        <button
                            key={index}
                            className={`page-button ${page === index ? 'active' : ''}`}
                            onClick={() => handlePageChange(index)}
                        >
                            {index + 1}
                        </button>
                    ))}
                </div>
            </div>
        </Template>
    );
}

export default Events;
