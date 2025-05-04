import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import Template from '../Template/Template.tsx';
import './AdminEvents.css';

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

function AdminEvents() {
    const { state } = useLocation();
    const successMessage = state?.successMessage;
    const navigate = useNavigate();

    const [token, setToken] = useState<string | null>(null);
    const [userRoles, setUserRoles] = useState<string[]>([]);
    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState<number>(0);
    const [totalPages, setTotalPages] = useState<number>(1);

    // 1. Parse token tylko raz
    useEffect(() => {
        const tokenFromCookie = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))?.split('=')[1] ?? null;

        if (!tokenFromCookie) {
            navigate('/login');
            return;
        }

        const user = parseJwt(tokenFromCookie);
        const roles = user?.roles || [];

        if (!roles.includes('ADMIN') && !roles.includes('MANAGER')) {
            navigate('/');
            return;
        }

        setToken(tokenFromCookie);
        setUserRoles(roles);
    }, [navigate]);

    // 2. Fetch eventów tylko jak zmieni się token i page
    useEffect(() => {
        if (!token) return; // czekamy aż token będzie ustawiony

        const fetchEvents = async () => {
            try {
                setLoading(true);

                const response = await fetch(`/api/event/administrate?page=${page}&size=5&sort=startDateTime,asc`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                    credentials: 'include',
                });

                if (!response.ok) {
                    throw new Error('Nie udało się pobrać wydarzeń.');
                }

                const data = await response.json();
                setEvents(data.content || []);
                setTotalPages(data.totalPages || 1);
            } catch (err: any) {
                setError(err.message || 'Wystąpił błąd podczas pobierania wydarzeń.');
            } finally {
                setLoading(false);
            }
        };

        fetchEvents();
    }, [token, page]); // teraz tylko token albo page zmieniają fetch

    const handleApprove = async (eventId: number) => {
        try {
            const response = await fetch(`/api/event/administrate/${eventId}/approve`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się zatwierdzić wydarzenia.');
            }

            setEvents(prevEvents => prevEvents.filter(event => event.eventId !== eventId));
        } catch (error) {
            console.error('Błąd podczas zatwierdzania:', error);
        }
    };

    const handleReject = async (eventId: number) => {
        try {
            const response = await fetch(`/api/event/administrate/${eventId}/reject`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się odrzucić wydarzenia.');
            }

            setEvents(prevEvents => prevEvents.filter(event => event.eventId !== eventId));
        } catch (error) {
            console.error('Błąd podczas odrzucania:', error);
        }
    };

    const handlePageChange = (newPage: number) => {
        setPage(newPage);
    };

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="admin-events-container">
                {successMessage && <div className="success-message">{successMessage}</div>}
                <button className={"btn btn-primary add-event-button"} onClick={() => navigate('/events')}>
                    Back
                </button>

                <h2>Oczekujące wydarzenia</h2>

                {loading && <p>Ładowanie wydarzeń...</p>}
                {error && <p className="error-message">{error}</p>}

                <div className="events-list">
                    {events.length === 0 && !loading ? (
                        <p>Brak wydarzeń do zatwierdzenia.</p>
                    ) : (
                        events.map((event) => (
                            <div key={event.eventId} className="event-card">
                                {event.imageUrl && (
                                    <img src={event.imageUrl} alt={event.eventName} className="event-image" />
                                )}
                                <h3>{event.eventName}</h3>
                                <p>{event.description}</p>
                                <p>
                                    <strong>Data:</strong> {new Date(event.startDateTime).toLocaleString()} - {new Date(event.endDateTime).toLocaleString()}
                                </p>
                                <p><strong>Lokalizacja:</strong> {event.location}</p>
                                <p><strong>Typ:</strong> {event.eventType}</p>
                                <p><strong>Dostępne miejsca:</strong> {event.maxParticipants - event.participantId.length}</p>

                                <div className="admin-buttons">
                                    <button className="btn approve-button" onClick={() => handleApprove(event.eventId)}>Zatwierdź</button>
                                    <button className="btn reject-button" onClick={() => handleReject(event.eventId)}>Odrzuć</button>
                                </div>
                            </div>
                        ))
                    )}
                </div>

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

export default AdminEvents;
