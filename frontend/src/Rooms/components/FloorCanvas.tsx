import React, { useState } from 'react';
import Room from './Room';
import AddSlot from './AddSlot';
import RoomModal from './RoomModal';
import './floor.css';

type RoomType = {
    id: string;
    number: string;
    capacity: number;
    floor: number;
};

const FloorCanvas: React.FC = () => {
    const [rooms, setRooms] = useState<RoomType[]>([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [insertIndex, setInsertIndex] = useState<number>(0);

    const handleOpenModal = (index: number) => {
        setInsertIndex(index);
        setModalOpen(true);
    };

    const handleAddRoom = (newRoom: RoomType) => {
        const updatedRooms = [...rooms];
        updatedRooms.splice(insertIndex, 0, newRoom);
        setRooms(updatedRooms);
        setModalOpen(false);
    };

    return (
        <div className="floor-container">
            {rooms.map((room, index) => (
                <React.Fragment key={room.id}>
                    <AddSlot onClick={() => handleOpenModal(index)} />
                    <Room room={room} />
                </React.Fragment>
            ))}
            <AddSlot onClick={() => handleOpenModal(rooms.length)} />
            {modalOpen && <RoomModal onAddRoom={handleAddRoom} onClose={() => setModalOpen(false)} />}
        </div>
    );
};

export default FloorCanvas;
