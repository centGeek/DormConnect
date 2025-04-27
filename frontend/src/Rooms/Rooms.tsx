import React, { useState } from "react";
import axios from "axios";
import Template from '../Template/Template.tsx';

axios.defaults.withCredentials = true;

const Room: React.FC = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [number, setNumber] = useState("");
    const [capacity, setCapacity] = useState(1);
    const [floor, setFloor] = useState(1);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const handleAddRoom = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await axios.post("/api/dorm/room", {
                number,
                capacity,
                floor,
                active: true,
                roomAssigns: [],
                groupedRooms: null
            });
            alert("Room created successfully!");
            setNumber("");
            setCapacity(1);
            setFloor(1);
            closeModal();
        } catch (error) {
            console.error(error);
            alert("Failed to create room.");
        }
    };

    return (
        <Template>
            <div className="floor-container">
                <h1>Floor Visualization</h1>
                <div className="floor-canvas" onClick={openModal}>
                    <p>Click here to add a room</p>
                </div>

                {isModalOpen && (
                    <div className="modal-overlay" onClick={closeModal}>
                        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                            <h2>Add New Room</h2>
                            <form onSubmit={handleAddRoom} className="room-form">
                                <div className="form-group">
                                    <label>Room Number:</label>
                                    <input
                                        type="text"
                                        value={number}
                                        onChange={(e) => setNumber(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Capacity:</label>
                                    <input
                                        type="number"
                                        value={capacity}
                                        min={1}
                                        onChange={(e) => setCapacity(parseInt(e.target.value))}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label>Floor:</label>
                                    <input
                                        type="number"
                                        value={floor}
                                        min={0}
                                        onChange={(e) => setFloor(parseInt(e.target.value))}
                                        required
                                    />
                                </div>
                                <div className="modal-buttons">
                                    <button type="submit" className="btn-primary">Save</button>
                                    <button type="button" className="btn-secondary" onClick={closeModal}>Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>
        </Template>
    );
};

export default Room;
