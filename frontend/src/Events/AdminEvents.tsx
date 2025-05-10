import { useEffect, useState, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import Template from '../Template/Template.tsx';
import './AdminEvents.css';
import Pagination from './Pagination.tsx';

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
    isApproved: boolean;
    approvalStatus: 'WAITING' | 'APPROVED' | 'DECLINED';
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
    const [activeTab, setActiveTab] = useState<'all' | 'approved' | 'declined' | 'waiting'>('waiting');

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

    const fetchEvents = useCallback(async (tab: 'all' | 'approved' | 'declined' | 'waiting', pageToFetch: number = page) => {
        if (!token) return;

        try {
            setLoading(true);

            let url = '/api/event/administrate';
            if (tab === 'approved') url += '/approved';
            else if (tab === 'declined') url += '/declined';
            else if (tab === 'waiting') url += '/waiting';

            url += `?page=${pageToFetch}&size=4&sort=startDateTime,asc`;

            const response = await fetch(url, {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${token}` },
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
    }, [token, page]);

    useEffect(() => {
        fetchEvents(activeTab);
    }, [fetchEvents, activeTab]);

    const handleApprove = async (eventId: number) => {
        try {
            const response = await fetch(`/api/event/administrate/${eventId}/approve`, {
                method: 'PUT',
                headers: { 'Authorization': `Bearer ${token}` },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się zatwierdzić wydarzenia.');
            }

            await fetchEvents(activeTab);
        } catch (error) {
            console.error('Błąd podczas zatwierdzania:', error);
        }
    };

    const handleReject = async (eventId: number) => {
        try {
            const response = await fetch(`/api/event/administrate/${eventId}/reject`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` },
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error('Nie udało się odrzucić wydarzenia.');
            }

            await fetchEvents(activeTab);
        } catch (error) {
            console.error('Błąd podczas odrzucania:', error);
        }
    };

    const handlePageChange = (newPage: number) => {
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
            fetchEvents(activeTab, newPage);
        }
    };

    const handleTabChange = (tab: 'all' | 'approved' | 'declined' | 'waiting') => {
        setActiveTab(tab);
        setPage(0);
    };

    const getStatusColor = (status: 'WAITING' | 'APPROVED' | 'DECLINED') => {
        switch (status) {
            case 'APPROVED':
                return 'green';
            case 'DECLINED':
                return 'red';
            case 'WAITING':
                return 'orange';
            default:
                return 'black';
        }
    };

    const getStatusLabel = (status: 'WAITING' | 'APPROVED' | 'DECLINED') => {
        switch (status) {
            case 'APPROVED':
                return 'Zatwierdzone';
            case 'DECLINED':
                return 'Odrzucone';
            case 'WAITING':
                return 'Oczekujące';
            default:
                return 'Nieznany';
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
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="admin-events-container">
                {successMessage && <div className="success-message">{successMessage}</div>}

                <button className="btn btn-primary add-event-button" onClick={() => navigate('/events')}>
                    Wróć
                </button>

                <div className="tabs">
                    <button
                        className={activeTab === 'waiting' ? 'active-tab' : ''}
                        onClick={() => handleTabChange('waiting')}
                    >
                        Oczekujące
                    </button>
                    <button
                        className={activeTab === 'approved' ? 'active-tab' : ''}
                        onClick={() => handleTabChange('approved')}
                    >
                        Zatwierdzone
                    </button>
                    <button
                        className={activeTab === 'declined' ? 'active-tab' : ''}
                        onClick={() => handleTabChange('declined')}
                    >
                        Odrzucone
                    </button>
                    <button
                        className={activeTab === 'all' ? 'active-tab' : ''}
                        onClick={() => handleTabChange('all')}
                    >
                        Wszystkie
                    </button>
                </div>

                <h2>Wydarzenia</h2>

                {loading && <p>Ładowanie wydarzeń...</p>}
                {error && <p className="error-message">{error}</p>}

                <div className="events-list">
                    {events.length === 0 && !loading ? (
                        <p>Brak wydarzeń do wyświetlenia.</p>
                    ) : (
                        events.map((event) => (
                            <div key={event.eventId} className="event-card">
                                {event.imageUrl && (
                                    <img src={event.imageUrl} alt={event.eventName} className="event-image" />
                                )}
                                <h3>{event.eventName}</h3>
                                <p>{event.description}</p>
                                <p>
                                    <strong>Data:</strong> {formatDate(event.startDateTime)} - {formatDate(event.endDateTime)}
                                </p>
                                <p><strong>Lokalizacja:</strong> {event.location}</p>
                                <p><strong>Typ:</strong> {event.eventType}</p>
                                <p><strong>Dostępne miejsca:</strong> {event.maxParticipants - event.participantId.length}</p>
                                <p>
                                    <strong>Status:</strong>{' '}
                                    <span className={`event-status ${event.approvalStatus.toLowerCase()}`}>
                                        {getStatusLabel(event.approvalStatus)}
                                    </span>
                                </p>

                                <div className="admin-buttons">
                                    <button className="btn approve-button" onClick={() => handleApprove(event.eventId)}>
                                        Zatwierdź
                                    </button>
                                    <button className="btn reject-button" onClick={() => handleReject(event.eventId)}>
                                        Odrzuć
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>

                <Pagination
                    totalPages={totalPages}
                    currentPage={page}
                    onPageChange={handlePageChange}
                />
            </div>
        </Template>
    );
}

export default AdminEvents;
