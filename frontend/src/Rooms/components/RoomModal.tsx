import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

type RoomModalProps = {
    onAddRoom: (room: { id: string; number: string; capacity: number; floor: number }) => void;
    onClose: () => void;
};

const RoomModal: React.FC<RoomModalProps> = ({ onAddRoom, onClose }) => {
    const [number, setNumber] = useState('');
    const [capacity, setCapacity] = useState(1);
    const [floor, setFloor] = useState(1);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onAddRoom({
            id: uuidv4(),
            number,
            capacity,
            floor
        });
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>Add Room</h2>
                <form onSubmit={handleSubmit} className="modal-form">
                    <input
                        type="text"
                        placeholder="Room Number"
                        value={number}
                        onChange={(e) => setNumber(e.target.value)}
                        required
                    />
                    <input
                        type="number"
                        placeholder="Capacity"
                        value={capacity}
                        onChange={(e) => setCapacity(parseInt(e.target.value))}
                        min={1}
                        required
                    />
                    <input
                        type="number"
                        placeholder="Floor"
                        value={floor}
                        onChange={(e) => setFloor(parseInt(e.target.value))}
                        required
                    />
                    <div className="modal-buttons">
                        <button type="submit" className="btn-primary">Add</button>
                        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RoomModal;
