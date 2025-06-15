import React, {useEffect, useState, useCallback, useContext} from 'react';
import {useNavigate, useLocation} from 'react-router-dom';
import Template from '../Template/Template.tsx';
import Pagination from './Pagination.tsx';
import {UserContext} from '../Context/UserContext.tsx';

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
    const {state} = useLocation();
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

    const user = useContext(UserContext);

    useEffect(() => {
        const tokenFromCookie = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))?.split('=')[1] ?? null;

        if (!tokenFromCookie) {
            navigate('/login');
            return;
        }

        // @ts-ignore
        if (user.user?.roles?.includes('ADMIN') && user.user?.roles?.includes('MANAGER')) {
            navigate('/');
            return;
        }

        setToken(tokenFromCookie);
    }, [navigate, user]);

    const translateApprovalStatus = (status: string) => {
        switch (status) {
            case 'WAITING':
                return 'Oczekujące';
            case 'APPROVED':
                return 'Zatwierdzone';
            case 'DECLINED':
                return 'Odrzucone';
            default:
                return status;
        }
    }
    const fetchEvents = useCallback(async () => {
        if (!token) return;

        try {
            setLoading(true);
            let url = `/api/event/administrate`;
            if (activeTab !== 'all') url += `/${activeTab}`;
            url += `?page=${page}&size=6&sort=${sortOption},${sortOrder}`;

            const response = await fetch(url, {
                method: 'GET',
                headers: {Authorization: `Bearer ${token}`},
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
            headers: {Authorization: `Bearer ${token}`},
            credentials: 'include',
        });
        fetchEvents();
    };

    const handleReject = async (eventId: number) => {
        if (!token) return;
        await fetch(`/api/event/administrate/${eventId}/reject`, {
            method: 'DELETE',
            headers: {Authorization: `Bearer ${token}`},
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
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
            <div className="relative p-2 sm:p-5 max-w-7xl mx-auto">
                <div className="w-full md:w-1/4 flex justify-center items-start p-5">
                    <button
                        type="button"
                        className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                        onClick={() => navigate(-1)}
                    >
                        ← Powrót
                    </button>
                </div>

                <div className="mt-16 sm:mt-20">
                    {successMessage && (
                        <div className="bg-gray-100 text-gray-700 p-3 rounded-lg mb-5 font-bold text-center">
                            {successMessage}
                        </div>
                    )}

                    <div className="flex flex-wrap justify-center gap-2 sm:gap-3 mb-5">
                        <button
                            className={`px-3 py-2 sm:px-4 sm:py-2 rounded-lg font-semibold ${
                                activeTab === 'waiting'
                                    ? 'bg-gray-500 text-white'
                                    : 'bg-gray-200 text-gray-500 border border-gray-500'
                            }`}
                            onClick={() => handleTabChange('waiting')}
                        >
                            Oczekujące
                        </button>
                        <button
                            className={`px-3 py-2 sm:px-4 sm:py-2 rounded-lg font-semibold ${
                                activeTab === 'approved'
                                    ? 'bg-gray-500 text-white'
                                    : 'bg-gray-200 text-gray-500 border border-gray-500'
                            }`}
                            onClick={() => handleTabChange('approved')}
                        >
                            Zatwierdzone
                        </button>
                        <button
                            className={`px-3 py-2 sm:px-4 sm:py-2 rounded-lg font-semibold ${
                                activeTab === 'declined'
                                    ? 'bg-gray-500 text-white'
                                    : 'bg-gray-200 text-gray-500 border border-gray-500'
                            }`}
                            onClick={() => handleTabChange('declined')}
                        >
                            Odrzucone
                        </button>
                        <button
                            className={`px-3 py-2 sm:px-4 sm:py-2 rounded-lg font-semibold ${
                                activeTab === 'all'
                                    ? 'bg-gray-500 text-white'
                                    : 'bg-gray-200 text-gray-500 border border-gray-500'
                            }`}
                            onClick={() => handleTabChange('all')}
                        >
                            Wszystkie
                        </button>
                    </div>

                    {!loading && events.length > 0 && (
                        <div className="text-center mb-5">
                            <select
                                value={`${sortOption},${sortOrder}`}
                                onChange={handleSortChange}
                                className="p-2 border border-gray-300 rounded-lg w-full max-w-xs"
                            >
                                <option value="startDateTime,asc">Data (rosnąco)</option>
                                <option value="startDateTime,desc">Data (malejąco)</option>
                                <option value="eventName,asc">Nazwa (A-Z)</option>
                                <option value="eventName,desc">Nazwa (Z-A)</option>
                            </select>
                        </div>
                    )}

                    {loading ? (
                        <p>Ładowanie...</p>
                    ) : error ? (
                        <p className="bg-red-100 text-red-600 p-3 rounded-lg">{error}</p>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                            {events.map(event => (
                                <div
                                    key={event.eventId}
                                    className="bg-white p-4 sm:p-5 rounded-lg shadow-md flex flex-col gap-2 sm:gap-3 hover:translate-y-[-5px] transition"
                                >
                                    {event.imageUrl && (
                                        <img
                                            src={event.imageUrl}
                                            alt={event.eventName}
                                            className="w-full h-40 sm:h-44 object-contain bg-gray-100 rounded-lg"
                                        />
                                    )}
                                    <h3 className="font-bold text-lg">{event.eventName}</h3>
                                    <p>
                                        <strong>Opis:</strong> {event.description}
                                    </p>
                                    <p>
                                        <strong>Data:</strong> {formatDate(event.startDateTime)} -{' '}
                                        {formatDate(event.endDateTime)}
                                    </p>
                                    <p>
                                        <strong>Miejsce:</strong> {event.location}
                                    </p>
                                    <p>
                                        <strong>Typ:</strong> {event.eventType}
                                    </p>
                                    <p>
                                        <strong>Miejsca:</strong>{' '}
                                        {event.maxParticipants - event.participantId.length}
                                    </p>
                                    <p>
                                        <strong>Status:</strong>{' '}
                                        <span
                                            className={`font-bold ${
                                                event.approvalStatus === 'APPROVED'
                                                    ? 'text-gray-500'
                                                    : event.approvalStatus === 'DECLINED'
                                                        ? 'text-gray-700'
                                                        : 'text-gray-400'
                                            }`}
                                        >
                                            {translateApprovalStatus(event.approvalStatus)}
                                        </span>
                                    </p>

                                    <div className="flex flex-col sm:flex-row gap-2 sm:gap-3 mt-auto">
                                        <button
                                            className="flex-1 bg-gray-500 text-white py-2 rounded-lg hover:bg-gray-600 transition"
                                            onClick={() => handleApprove(event.eventId)}
                                        >
                                            Zatwierdź
                                        </button>
                                        <button
                                            className="flex-1 bg-gray-700 text-white py-2 rounded-lg hover:bg-gray-800 transition"
                                            onClick={() => handleReject(event.eventId)}
                                        >
                                            Odrzuć
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}

                    {!loading && events.length > 0 && totalPages > 1 && (
                        <Pagination
                            totalPages={totalPages}
                            currentPage={page}
                            onPageChange={setPage}
                        />
                    )}
                </div>
            </div>
        </Template>
    );
}

export default AdminEvents;