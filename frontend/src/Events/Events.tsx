import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Template from '../Template/Template.tsx';
import './Events.css';

// Event interface
interface Event {
    eventId: number;
    eventName: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
    location: string;
    eventType: string;
    maxParticipants: number;
    participantId: number[]; // Lista uczestników
}

// Funkcja do dekodowania tokena JWT
function parseJwt(token: string) {
    try {
        const base64Url = token.split('.')[1]; // Payload znajduje się w drugiej części (indeks 1)
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Zmieniamy format base64, by był poprawny
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        // Parsujemy payload jako JSON
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Invalid token", e);
        return null;
    }
}


function Events() {
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
                throw new Error('Failed to fetch events');
            }

            const data = await response.json();
            setEvents(data.content || []);
            setTotalPages(data.totalPages);
        } catch (error: any) {
            console.error('Error fetching events:', error);
            setError(error.message || 'An error occurred while fetching events.');
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

    const joinEvent = async (eventId: number) => {
        try {
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event/participant/${eventId}`, {
                method: 'PUT',  // Poprawnie używamy POST do dołączenia
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Failed to join event');
            }

            fetchEvents(page); // Odświeżamy listę wydarzeń
        } catch (error: any) {
            console.error('Error joining event:', error);
        }
    };

    const leaveEvent = async (eventId: number) => {
        try {
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event/participant/${eventId}`, {
                method: 'DELETE', // Używamy DELETE do opuszczenia wydarzenia
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Failed to leave event');
            }

            fetchEvents(page); // Odświeżamy listę wydarzeń
        } catch (error: any) {
            console.error('Error leaving event:', error);
        }
    };

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="events-container">
                <h2>All Events</h2>
                <button className="btn btn-primary add-event-button" onClick={handleAddEvent}>
                    Add Event
                </button>

                {loading && <p>Loading events...</p>}
                {error && <p className="error-message">{error}</p>}

                <div className="events-list">
                    {events.length === 0 ? (
                        <p>No events available.</p>
                    ) : (
                        events.map((event) => (
                            <div key={event.eventId} className="event-card">
                                <h3>{event.eventName}</h3>
                                <p>{event.description}</p>
                                <p>
                                    <strong>Date:</strong> {new Date(event.startDateTime).toLocaleString()} -{' '}
                                    {new Date(event.endDateTime).toLocaleString()}
                                </p>
                                <p><strong>Location:</strong> {event.location}</p>
                                <p><strong>Type:</strong> {event.eventType}</p>
                                <p><strong>Available Seats:</strong> {event.maxParticipants}</p>

                                {/* Przyciski Join/Leave */}
                                {userId && (
                                    event.participantId.includes(userId) ? (
                                        <button className="btn leave-button" onClick={() => leaveEvent(event.eventId)}>
                                            Leave Event
                                        </button>
                                    ) : (
                                        <button className="btn join-button" onClick={() => joinEvent(event.eventId)}>
                                            Join Event
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
