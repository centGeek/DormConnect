import React, { useState, useEffect, useContext, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import Template from '../Template/Template';
import EventCard from './EventsCard';
import Pagination from './Pagination';
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
}

const Events = () => {
    const { state } = useLocation();
    const successMessage = state?.successMessage;

    const [events, setEvents] = useState<Event[]>([]);
    const [organizedEvents, setOrganizedEvents] = useState<Event[]>([]);
    const [participatingEvents, setParticipatingEvents] = useState<Event[]>([]);

    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const [totalPages, setTotalPages] = useState<number>(0);
    const [page, setPage] = useState<number>(0);

    const [activeTab, setActiveTab] = useState<'organized' | 'participating' | 'all'>('participating');
    const [sortType, setSortType] = useState<string>('startDateTime,asc');

    const navigate = useNavigate();
    const userContext = useContext(UserContext);

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    const userId = userContext?.user?.id;
    const isAdmin = userContext?.user?.roles.some(role => ['ADMIN', 'MANAGER'].includes(role));

    const handleAddEvent = () => {
        navigate('/events/create');
    };

    const handleAdminNavigation = () => {
        navigate('/events/admin/AdminEvents');
    };

    const fetchEvents = useCallback(async (tab: 'organized' | 'participating' | 'all', pageToFetch: number = 0) => {
        if (!token) {
            navigate('/login');
            return;
        }

        try {
            setLoading(true);
            let url = `/api/event?page=${pageToFetch}&size=6&sort=${sortType}`;

            if (tab === 'organized') {
                url = `/api/event/organizer?page=${pageToFetch}&size=6&sort=${sortType}`;
            } else if (tab === 'participating') {
                url = `/api/event/participant?page=${pageToFetch}&size=6&sort=${sortType}`;
            }

            const response = await fetch(url, {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${token}` },
                credentials: 'include',
            });

            if (!response.ok) throw new Error('Nie udało się pobrać wydarzeń.');

            const data = await response.json();

            if (tab === 'organized') {
                setOrganizedEvents(data.content || []);
            } else if (tab === 'participating') {
                setParticipatingEvents(data.content || []);
            } else {
                setEvents(data.content || []);
            }

            setTotalPages(data.totalPages || 1);
        } catch (error: any) {
            setError(error.message || 'Wystąpił błąd.');
        } finally {
            setLoading(false);
        }
    }, [token, navigate, sortType]);

    useEffect(() => {
        fetchEvents('participating', 0);
        fetchEvents('organized', 0);
    }, []);

    useEffect(() => {
        fetchEvents(activeTab, page);
    }, [fetchEvents, activeTab, page]);

    const handlePageChange = (newPage: number) => {
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
            fetchEvents(activeTab, newPage);
        }
    };

    const handleTabChange = (tab: 'organized' | 'participating' | 'all') => {
        setActiveTab(tab);
        setPage(0);
    };

    const handleSortChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSortType(event.target.value);
        setPage(0);
    };

    const getEventsToDisplay = () => {
        if (activeTab === 'organized') return organizedEvents;
        if (activeTab === 'participating') return participatingEvents;
        return events;
    };

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms'},
            {text: 'Zgłoś problem', link: '/problems'},
        ]}>
            <div className="p-6">
                {successMessage && <div className="bg-green-100 text-green-700 p-4 rounded-lg mb-4 justify-center">{successMessage}</div>}

                <h2 className="text-4xl font-bold text-gray-800 mb-6 text-center">Events</h2>

                <div className="flex flex-wrap gap-4 mb-6 justify-center">
                    <button
                        className="bg-gray-500 text-white border  px-4 py-2 rounded-lg shadow hover:bg-white hover:text-gray-500 transition"
                        onClick={handleAddEvent}
                    >
                        Dodaj wydarzenie
                    </button>
                    {isAdmin && (
                        <button
                            className="bg-gray-500 text-white px-4 border border-gray py-2 rounded-lg shadow hover:bg-white hover:text-gray-500 transition"
                            onClick={handleAdminNavigation}
                        >
                            Panel administratora
                        </button>
                    )}
                </div>

                <div className="flex gap-4 mb-6 justify-center  bg-gray-100">
                    <button
                        className={`px-4 py-2 rounded-lg shadow transition ${
                            activeTab === 'organized' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-400 hover:text-white'
                        }`}
                        onClick={() => handleTabChange('organized')}
                    >
                        Organizowane
                    </button>
                    <button
                        className={`px-4 py-2 rounded-lg shadow transition ${
                            activeTab === 'participating' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-400 hover:text-white'
                        }`}
                        onClick={() => handleTabChange('participating')}
                    >
                        Moje
                    </button>
                    <button
                        className={`px-4 py-2 rounded-lg shadow transition ${
                            activeTab === 'all' ? 'bg-gray-800 text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-400 hover:text-white'
                        }`}
                        onClick={() => handleTabChange('all')}
                    >
                        Wszystkie
                    </button>
                </div>

                {loading && <p className="text-gray-500">Ładowanie wydarzeń...</p>}
                {error && <p className="text-red-500">{error}</p>}

                {!loading && getEventsToDisplay().length > 0 && (
                    <div className="mb-6 ">
                        <select
                            className="border border-gray-300 rounded-lg px-4 py-2"
                            onChange={handleSortChange}
                            value={sortType}
                        >
                            <option value="startDateTime,asc">Data (rosnąco)</option>
                            <option value="startDateTime,desc">Data (malejąco)</option>
                            <option value="eventName,asc">Nazwa (A-Z)</option>
                            <option value="eventName,desc">Nazwa (Z-A)</option>
                        </select>
                    </div>
                )}

                {getEventsToDisplay().length === 0 && !loading ? (
                    <p className="text-gray-500 text-center">Brak wydarzeń do wyświetlenia.</p>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 bo">
                        {getEventsToDisplay().map(event => (
                            <EventCard
                                key={event.eventId}
                                event={event}
                                userId={userId}
                                isOrganizer={activeTab === 'organized'}
                                onEventDeleted={() => fetchEvents(activeTab, page)}
                            />
                        ))}
                    </div>
                )}

                {!loading && getEventsToDisplay().length > 0 && totalPages > 1 && (
                    <Pagination
                        totalPages={totalPages}
                        currentPage={page}
                        onPageChange={handlePageChange}
                    />
                )}
            </div>
        </Template>
    );
};

export default Events;