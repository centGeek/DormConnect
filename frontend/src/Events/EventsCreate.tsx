import Template from '../Template/Template.tsx';
import React, { useState } from 'react';
import './EventsCreate.css';

function EventsCreate(){
    const [eventName, setEventName] = useState('');
    const [eventDescription, setEventDescription] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [location, setLocation] = useState('');
    const [eventType, setEventType] = useState('');
    const [availableSeats, setAvailableSeats] = useState<number | ''>('');
    const [image, setImage] = useState<File | null>(null);

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('eventName', eventName);
        formData.append('eventDescription', eventDescription);
        formData.append('startDate', startDate);
        formData.append('endDate', endDate);
        formData.append('location', location);
        formData.append('eventType', eventType);
        formData.append('availableSeats', availableSeats.toString());
        if (image) {
            formData.append('image', image);
        }
        console.log('Form submitted', formData);
        // Add logic to send to server
    };
    return(
        <Template
            buttons={[{text: 'Home', link: '/home' }, { text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
        footerContent={<p></p>}>
            <div className="events-create-container">
                <h2>Create event</h2>
                <form onSubmit={handleSubmit} className="event-form">
                    <div className="form-group">
                        <label htmlFor="eventName">Event's name</label>
                        <input
                            type="text"
                            id="eventName"
                            value={eventName}
                            onChange={(e) => setEventName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="eventDescription">Description</label>
                        <textarea
                            id="eventDescription"
                            value={eventDescription}
                            onChange={(e) => setEventDescription(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="startDate">Start date</label>
                        <input
                            type="datetime-local"
                            id="startDate"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="endDate">End date</label>
                        <input
                            type="datetime-local"
                            id="endDate"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="location">Place</label>
                        <input
                            type="text"
                            id="location"
                            value={location}
                            onChange={(e) => setLocation(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="eventType">Type</label>
                        <select
                            id="eventType"
                            value={eventType}
                            onChange={(e) => setEventType(e.target.value)}
                            required
                        >
                            <option value="">Choose type</option>
                            <option value="party">Party</option>
                            <option value="meeting">Meeting</option>
                            <option value="workshop">Workshop</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="availableSeats">Number of people</label>
                        <input
                            type="number"
                            id="availableSeats"
                            value={availableSeats}
                            onChange={(e) => setAvailableSeats(Number(e.target.value))}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="image">Image</label>
                        <input
                            type="file"
                            id="image"
                            accept="image/*"
                            onChange={(e) => setImage(e.target.files ? e.target.files[0] : null)}
                        />
                    </div>
                    <button type="submit" className="btn btn-primary">Create event</button>
                </form>
            </div>
        </Template>
    )
}
export default EventsCreate;