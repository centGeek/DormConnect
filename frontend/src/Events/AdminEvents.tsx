import React, { useEffect, useState, useCallback, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import Template from '../Template/Template.tsx';
import './AdminEvents.css';
import Pagination from './Pagination.tsx';
import { UserContext } from '../Context/UserContext.tsx';

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
    approvalStatus: 'WAITING' | 'APPROVED' | 'DECLINED';
}

function AdminEvents() {
    const { state } = useLocation();
    const successMessage = state?.successMessage;
    const navigate = useNavigate();

    const [token, setToken] = useState<string | null>(null);
    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [activeTab, setActiveTab] = useState<'all' | 'approved' | 'declined' | 'waiting'>('waiting');
    const [sortOption, setSortOption] = useState<'startDateTime' | 'eventName'>('startDateTime');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

    const { user } = useContext(UserContext);

    useEffect(() => {
        const tokenFromCookie = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))?.split('=')[1] ?? null;

        if (!tokenFromCookie) {
            navigate('/login');
            return;
        }

        if (!user?.roles?.includes('ADMIN') && !user?.roles?.includes('MANAGER')) {
            navigate('/');
            return;
        }

        setToken(tokenFromCookie);
    }, [navigate, user]);

    const fetchEvents = useCallback(async () => {
        if (!token) return;

        try {
            setLoading(true);
            let url = `/api/event/administrate`;
            if (activeTab !== 'all') url += `/${activeTab}`;
            url += `?page=${page}&size=6&sort=${sortOption},${sortOrder}`;

            const response = await fetch(url, {
                method: 'GET',
                headers: { Authorization: `Bearer ${token}` },
                credentials: 'include',
            });

            if (!response.ok) throw new Error('Błąd podczas pobierania wydarzeń.');

            const data = await response.json();
            setEvents(data.content || []);
            setTotalPages(data.totalPages || 1);
        } catch (err: any) {
            setError(err.message || 'Wystąpił błąd.');
        } finally {
            setLoading(false);
        }
    }, [token, activeTab, page, sortOption, sortOrder]);

    useEffect(() => {
        fetchEvents();
    }, [fetchEvents]);

    const handleApprove = async (eventId: number) => {
        if (!token) return;
        await fetch(`/api/event/administrate/${eventId}/approve`, {
            method: 'PUT',
            headers: { Authorization: `Bearer ${token}` },
            credentials: 'include',
        });
        fetchEvents();
    };

    const handleReject = async (eventId: number) => {
        if (!token) return;
        await fetch(`/api/event/administrate/${eventId}/reject`, {
            method: 'DELETE',
            headers: { Authorization: `Bearer ${token}` },
            credentials: 'include',
        });
        fetchEvents();
    };

    const handleTabChange = (tab: typeof activeTab) => {
        setActiveTab(tab);
        setPage(0);
    };

    const handleSortChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const [field, order] = e.target.value.split(',');
        setSortOption(field as 'startDateTime' | 'eventName');
        setSortOrder(order as 'asc' | 'desc');
        setPage(0);
    };

    const formatDate = (date: string) =>
        new Date(date).toLocaleString('pl-PL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });

    return (
        <Template buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}>
            <div className="admin-events-container">
                {/* Wróć button */}
                <div className="back-button-container">
                    <button className="add-event-button" onClick={() => navigate('/events')}>Wróć</button>
                </div>

                {/* Główna zawartość */}
                <div className="admin-content">
                    {successMessage && <div className="success-message">{successMessage}</div>}

                    <div className="tabs">
                        <button className={activeTab === 'waiting' ? 'active-tab' : ''} onClick={() => handleTabChange('waiting')}>Oczekujące</button>
                        <button className={activeTab === 'approved' ? 'active-tab' : ''} onClick={() => handleTabChange('approved')}>Zatwierdzone</button>
                        <button className={activeTab === 'declined' ? 'active-tab' : ''} onClick={() => handleTabChange('declined')}>Odrzucone</button>
                        <button className={activeTab === 'all' ? 'active-tab' : ''} onClick={() => handleTabChange('all')}>Wszystkie</button>
                    </div>

                    {!loading && events.length > 0 && (
                        <div className="sort-container">
                            <select value={`${sortOption},${sortOrder}`} onChange={handleSortChange}>
                                <option value="startDateTime,asc">Data (rosnąco)</option>
                                <option value="startDateTime,desc">Data (malejąco)</option>
                                <option value="eventName,asc">Nazwa (A-Z)</option>
                                <option value="eventName,desc">Nazwa (Z-A)</option>
                            </select>
                        </div>
                    )}

                    {loading ? <p>Ładowanie...</p> : error ? <p className="error-message">{error}</p> : (
                        <div className="events-grid">
                            {events.map(event => (
                                <div key={event.eventId} className="event-card">
                                    {event.imageUrl && <img src={event.imageUrl} alt={event.eventName} className="event-image" />}
                                    <h3>{event.eventName}</h3>
                                    <p><strong>Opis:</strong> {event.description}</p>
                                    <p><strong>Data:</strong> {formatDate(event.startDateTime)} - {formatDate(event.endDateTime)}</p>
                                    <p><strong>Miejsce:</strong> {event.location}</p>
                                    <p><strong>Typ:</strong> {event.eventType}</p>
                                    <p><strong>Miejsca:</strong> {event.maxParticipants - event.participantId.length}</p>
                                    <p><strong>Status:</strong> <span className={`status ${event.approvalStatus.toLowerCase()}`}>{event.approvalStatus}</span></p>

                                    <div className="admin-buttons">
                                        <button className="approve-button" onClick={() => handleApprove(event.eventId)}>Zatwierdź</button>
                                        <button className="reject-button" onClick={() => handleReject(event.eventId)}>Odrzuć</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}

                    {!loading && events.length > 0 && totalPages > 1 && (
                        <Pagination
                            totalPages={totalPages}
                            currentPage={page}
                            onPageChange={handlePageChange}
                        />
                    )}
                </div>
            </div>
        </Template>
    );
}

export default AdminEvents;
