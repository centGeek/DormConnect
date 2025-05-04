import React from 'react';
import EventCard from './EventsCard';

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

interface EventListProps {
    events: Event[];
    userId: number | undefined;
    joinEvent: (eventId: number) => void;
    leaveEvent: (eventId: number) => void;
}

const EventList: React.FC<EventListProps> = ({ events, userId, joinEvent, leaveEvent }) => {
    return (
        <div className="events-list">
            {events.map(event => (
                <EventCard
                    key={event.eventId}
                    event={event}
                    userId={userId}
                    joinEvent={joinEvent}
                    leaveEvent={leaveEvent}
                />
            ))}
        </div>
    );
};

export default EventList;
