import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import Template from '../Template/Template';
import EventCard from './EventsCard';
import Pagination from './Pagination';
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
}

const Events = () => {
    const { state } = useLocation();
    const successMessage = state?.successMessage;

    const [events, setEvents] = useState<Event[]>([]);
    const [organizedEvents, setOrganizedEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [isOrganizer, setIsOrganizer] = useState<boolean>(false);

    const [totalEventPages, setTotalEventPages] = useState<number>(0);
    const [totalOrganizedPages, setTotalOrganizedPages] = useState<number>(0);
    const [eventPage, setEventPage] = useState<number>(0);
    const [organizedPage, setOrganizedPage] = useState<number>(0);

    const [showOrganized, setShowOrganized] = useState<boolean>(false);
    const [showAll, setShowAll] = useState<boolean>(false);

    const navigate = useNavigate();

    const token = document.cookie
        .split('; ')
        .find(row => row.startsWith('token='))?.split('=')[1];

    const user = token ? parseJwt(token) : null;
    const userId = user?.id;

    const fetchEvents = async (page: number = 0) => {
        try {
            setLoading(true);

            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event?page=${page}&size=4&sort=startDateTime,asc`, {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${token}` },
                credentials: 'include',
            });

            if (!response.ok) throw new Error('Nie udało się pobrać wydarzeń');

            const data = await response.json();
            setEvents(data.content || []);
            setTotalEventPages(data.totalPages || 0);
        } catch (error: any) {
            setError(error.message || 'Wystąpił błąd.');
        } finally {
            setLoading(false);
        }
    };

    const fetchOrganizedEvents = async (page: number = 0) => {
        try {
            if (!token) {
                navigate('/login');
                return;
            }

            const response = await fetch(`/api/event/organizer?page=${page}&size=4`, {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${token}` },
                credentials: 'include',
            });

            if (!response.ok) throw new Error('Nie udało się pobrać organizowanych wydarzeń');

            const data = await response.json();
            setOrganizedEvents(data.content || []);
            setTotalOrganizedPages(data.totalPages || 0);
            setIsOrganizer((data.content || []).length > 0);
        } catch (error: any) {
            console.error('Błąd pobierania organizowanych wydarzeń:', error);
        }
    };

    const handleAddEvent = () => {
        navigate('/events/create');
    };

    useEffect(() => {
        fetchEvents();
        fetchOrganizedEvents();
    }, []);

    return (
        <Template
            buttons={[{ text: 'Home', link: '/home' }, { text: 'Chat', link: '/chat' }]}
            footerContent={<p>&copy; 2025 Wanderer. All rights reserved.</p>}
        >
            <div className="events-container">
                {successMessage && <div className="success-message">{successMessage}</div>}

                <h2>Wydarzenia</h2>
                <button className="btn btn-primary add-event-button" onClick={handleAddEvent}>
                    Dodaj wydarzenie
                </button>

                {loading && <p>Ładowanie wydarzeń...</p>}
                {error && <p className="error-message">{error}</p>}

                {/* ORGANIZED EVENTS */}
                {isOrganizer && (
                    <div className="events-section">
                        <button className="toggle-button" onClick={() => setShowOrganized(!showOrganized)}>
                            {showOrganized ? 'Ukryj organizowane wydarzenia' : 'Pokaż organizowane wydarzenia'}
                        </button>
                        {showOrganized && (
                            <>
                                <div className="events-grid">
                                    {organizedEvents.map(event => (
                                        <EventCard key={event.eventId} event={event} userId={userId} isOrganizer={true} />
                                    ))}
                                </div>
                                <Pagination
                                    totalPages={totalOrganizedPages}
                                    currentPage={organizedPage}
                                    onPageChange={(page) => {
                                        setOrganizedPage(page);
                                        fetchOrganizedEvents(page);
                                    }}
                                />
                            </>
                        )}
                    </div>
                )}

                {/* ALL EVENTS */}
                <div className="events-section">
                    <button className="toggle-button" onClick={() => setShowAll(!showAll)}>
                        {showAll ? 'Ukryj wszystkie wydarzenia' : 'Pokaż wszystkie wydarzenia'}
                    </button>
                    {showAll && (
                        <>
                            <div className="events-grid">
                                {events.map(event => (
                                    <EventCard key={event.eventId} event={event} userId={userId} isOrganizer={false} />
                                ))}
                            </div>
                            <Pagination
                                totalPages={totalEventPages}
                                currentPage={eventPage}
                                onPageChange={(page) => {
                                    setEventPage(page);
                                    fetchEvents(page);
                                }}
                            />
                        </>
                    )}
                </div>
            </div>
        </Template>
    );
};

export default Events;
