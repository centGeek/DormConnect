import React from 'react';
import './EventsCard.css';

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
    userId?: number;
    joinEvent: (eventId: number) => void;
    leaveEvent: (eventId: number) => void;
}

const EventsCard: React.FC<Event> = ({
                                         eventId, eventName, description, startDateTime, endDateTime, location, eventType,
                                         maxParticipants, participantId, imageUrl, availableSpots, userId, joinEvent, leaveEvent
                                     }) => {

    const availableSpotsCalculated = maxParticipants - participantId.length;

    return (
        <div className="event-card">
            {imageUrl && <img src={imageUrl} alt={eventName} className="event-image" />}
            <h3>{eventName}</h3>
            <p>{description}</p>
            <p><strong>Data:</strong> {new Date(startDateTime).toLocaleString()} - {new Date(endDateTime).toLocaleString()}</p>
            <p><strong>Lokalizacja:</strong> {location}</p>
            <p><strong>Typ:</strong> {eventType}</p>
            <p><strong>Dostępne miejsca:</strong> {availableSpotsCalculated}</p>

            {userId && (
                participantId.includes(userId) ? (
                    <button className="btn leave-button" onClick={() => leaveEvent(eventId)}>
                        Opuść wydarzenie
                    </button>
                ) : (
                    <button
                        className="btn join-button"
                        onClick={() => joinEvent(eventId)}
                        disabled={availableSpotsCalculated <= 0}
                    >
                        Dołącz do wydarzenia
                    </button>
                )
            )}
        </div>
    );
};

export default EventsCard;
