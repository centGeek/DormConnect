import React, { useState, useEffect } from 'react';
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
    userId?: number;
    editEvent?: (eventId: number) => void;
    isOrganizerSection?: boolean;
}

const EventsCard: React.FC<Event> = ({
                                         eventId, eventName, description, startDateTime, endDateTime, location, eventType,
                                         maxParticipants, participantId, imageUrl, userId, editEvent, isOrganizerSection
                                     }) => {

    // Stan dla liczby dostępnych miejsc
    const [availableSpots, setAvailableSpots] = useState<number>(maxParticipants - participantId.length);
    const [participants, setParticipants] = useState<number[]>(participantId);

    // Używamy useEffect do aktualizacji dostępnych miejsc w przypadku zmiany uczestników
    useEffect(() => {
        setAvailableSpots(maxParticipants - participants.length);
    }, [maxParticipants, participants]);

    // Funkcja dołączania do wydarzenia
    const joinEvent = () => {
        if (availableSpots > 0 && userId && !participants.includes(userId)) {
            setParticipants([...participants, userId]);  // Dodajemy użytkownika do listy uczestników
        }
    };

    // Funkcja opuszczania wydarzenia
    const leaveEvent = () => {
        if (userId && participants.includes(userId)) {
            setParticipants(participants.filter(id => id !== userId));  // Usuwamy użytkownika z listy uczestników
        }
    };

    return (
        <div className="event-card">
            {imageUrl && <img src={imageUrl} alt={eventName} className="event-image" />}
            <h3>{eventName}</h3>
            <p>{description}</p>
            <p><strong>Data:</strong> {new Date(startDateTime).toLocaleString()} - {new Date(endDateTime).toLocaleString()}</p>
            <p><strong>Lokalizacja:</strong> {location}</p>
            <p><strong>Typ:</strong> {eventType}</p>
            <p><strong>Dostępne miejsca:</strong> {availableSpots}</p>

            {isOrganizerSection ? (
                <button className="btn edit-button" onClick={() => editEvent && editEvent(eventId)}>
                    Edytuj
                </button>
            ) : (
                userId && (
                    participants.includes(userId) ? (
                        <button className="btn leave-button" onClick={leaveEvent}>
                            Opuść wydarzenie
                        </button>
                    ) : (
                        <button
                            className="btn join-button"
                            onClick={joinEvent}
                            disabled={availableSpots <= 0}
                        >
                            Dołącz do wydarzenia
                        </button>
                    )
                )
            )}
        </div>
    );
};

export default EventsCard;
