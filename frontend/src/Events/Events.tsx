import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Template from '../Template/Template.tsx';
import './Events.css';

interface Event {
    id: number;
    name: string;
    description: string;
    startDate: string;
    endDate: string;
    location: string;
    type: string;
    availableSeats: number;
}

function Events() {
    const [events, setEvents] = useState<Event[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Replace mockData with API call to fetch events
        const mockData: Event[] = [
            {
                id: 1,
                name: 'React Workshop',
                description: 'Learn the basics of React',
                startDate: '2023-12-01T10:00',
                endDate: '2023-12-01T14:00',
                location: 'Room 101',
                type: 'workshop',
                availableSeats: 20,
            },
            {
                id: 2,
                name: 'Integration Meeting',
                description: 'Meeting for new students',
                startDate: '2023-12-05T18:00',
                endDate: '2023-12-05T20:00',
                location: 'Outdoor area',
                type: 'meeting',
                availableSeats: 50,
            },
        ];
        setEvents(mockData);
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
                <h2>All Events</h2>
                <button className="btn btn-primary add-event-button" onClick={handleAddEvent}>
                    Add Event
                </button>
                <div className="events-list">
                    {events.map((event) => (
                        <div key={event.id} className="event-card">
                            <h3>{event.name}</h3>
                            <p>{event.description}</p>
                            <p>
                                <strong>Date:</strong> {new Date(event.startDate).toLocaleString()} -{' '}
                                {new Date(event.endDate).toLocaleString()}
                            </p>
                            <p>
                                <strong>Location:</strong> {event.location}
                            </p>
                            <p>
                                <strong>Type:</strong> {event.type}
                            </p>
                            <p>
                                <strong>Available Seats:</strong> {event.availableSeats}
                            </p>
                        </div>
                    ))}
                </div>
            </div>
        </Template>
    );
}

export default Events;