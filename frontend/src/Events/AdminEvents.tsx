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
    isApproved: boolean; // Dodane
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

    useEffect(() => {
        if (!token) return;

        const fetchEvents = async () => {
            try {
                setLoading(true);

                const response = await fetch(`/api/event/administrate?page=${page}&size=4&sort=startDateTime,asc`, {
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
    }, [token, page]);

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
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
        }
    };

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="admin-events-container">
                {successMessage && <div className="success-message">{successMessage}</div>}

                <button className="btn btn-primary add-event-button" onClick={() => navigate('/events')}>
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
                                <p>
                                    <strong>Status:</strong>{' '}
                                    <span style={{ color: event.isApproved ? 'green' : 'red', fontWeight: 'bold' }}>
                                        {event.isApproved ? 'Zatwierdzone' : 'Oczekujące'}
                                    </span>
                                </p>

                                <div className="admin-buttons">
                                    <button className="btn approve-button" onClick={() => handleApprove(event.eventId)}>Zatwierdź</button>
                                    <button className="btn reject-button" onClick={() => handleReject(event.eventId)}>Odrzuć</button>
                                </div>
                            </div>
                        ))
                    )}
                </div>

                <div className="pagination">
                    <button
                        className="page-button"
                        onClick={() => handlePageChange(0)}
                        disabled={page === 0}
                    >
                        &lt;&lt;
                    </button>
                    <button
                        className="page-button"
                        onClick={() => handlePageChange(page - 1)}
                        disabled={page === 0}
                    >
                        &lt;
                    </button>

                    {(() => {
                        const maxPagesToShow = 3;
                        let startPage = Math.max(0, page - Math.floor(maxPagesToShow / 2));
                        let endPage = startPage + maxPagesToShow - 1;

                        if (endPage >= totalPages) {
                            endPage = totalPages - 1;
                            startPage = Math.max(0, endPage - maxPagesToShow + 1);
                        }

                        const pageNumbers = [];
                        for (let i = startPage; i <= endPage; i++) {
                            pageNumbers.push(i);
                        }

                        return pageNumbers.map(p => (
                            <button
                                key={p}
                                className={`page-button ${page === p ? 'active' : ''}`}
                                onClick={() => handlePageChange(p)}
                            >
                                {p + 1}
                            </button>
                        ));
                    })()}

                    <button
                        className="page-button"
                        onClick={() => handlePageChange(page + 1)}
                        disabled={page === totalPages - 1}
                    >
                        &gt;
                    </button>
                    <button
                        className="page-button"
                        onClick={() => handlePageChange(totalPages - 1)}
                        disabled={page === totalPages - 1}
                    >
                        &gt;&gt;
                    </button>
                </div>
            </div>
        </Template>
    );
}

export default AdminEvents;
