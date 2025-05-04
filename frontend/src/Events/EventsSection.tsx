import React, { useState } from 'react';
import EventsList from './EventsList';
import './EventsSection.css';

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

interface EventsSectionProps {
    title: string;
    events: Event[];
    joinEvent?: (eventId: number) => void;
    leaveEvent?: (eventId: number) => void;
    editEvent?: (eventId: number) => void;
    userId: number | undefined;
    pageSize: number;
    isOrganizerSection?: boolean; // <- Nowy prop
}

const EventsSection: React.FC<EventsSectionProps> = ({
                                                         title,
                                                         events,
                                                         joinEvent,
                                                         leaveEvent,
                                                         editEvent,
                                                         userId,
                                                         pageSize,
                                                         isOrganizerSection,
                                                     }) => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const toggleList = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div className="events-section">
            <h3 onClick={toggleList} className="toggle-section">
                {title} {isOpen ? '▼' : '▲'}
            </h3>
            {isOpen && (
                <EventsList
                    events={events}
                    userId={userId}
                    joinEvent={joinEvent}
                    leaveEvent={leaveEvent}
                    editEvent={editEvent}
                    pageSize={pageSize}
                    isOrganizerSection={isOrganizerSection}
                />
            )}
        </div>
    );
};

export default EventsSection;
