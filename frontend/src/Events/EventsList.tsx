import React, { useState } from 'react';
import EventsCard from './EventsCard';
import Pagination from './Pagination';
import './EventsList.css';

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
    availableSpots: number;
}

interface EventsListProps {
    events: Event[];
    userId: number | undefined;
    joinEvent: (eventId: number) => void;
    leaveEvent: (eventId: number) => void;
    pageSize: number;
}

const EventsList: React.FC<EventsListProps> = ({ events, userId, joinEvent, leaveEvent, pageSize }) => {
    const [currentPage, setCurrentPage] = useState<number>(0);
    const totalPages = Math.ceil(events.length / pageSize);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    const eventsToDisplay = events.slice(currentPage * pageSize, (currentPage + 1) * pageSize);

    return (
        <div className="events-list">
            <div className="events-grid">
                {eventsToDisplay.map((event) => (
                    <EventsCard
                        key={event.eventId}
                        eventId={event.eventId}
                        eventName={event.eventName}
                        description={event.description}
                        startDateTime={event.startDateTime}
                        endDateTime={event.endDateTime}
                        location={event.location}
                        eventType={event.eventType}
                        maxParticipants={event.maxParticipants}
                        participantId={event.participantId}
                        imageUrl={event.imageUrl}
                        availableSpots={event.availableSpots}
                        userId={userId}
                        joinEvent={joinEvent}
                        leaveEvent={leaveEvent}
                    />
                ))}
            </div>

            <Pagination totalPages={totalPages} currentPage={currentPage} onPageChange={handlePageChange} />
        </div>
    );
};

export default EventsList;
