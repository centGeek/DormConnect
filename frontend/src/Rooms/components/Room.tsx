import React from 'react';

type RoomProps = {
    room: {
        id: string;
        number: string;
        capacity: number;
        floor: number;
    };
};

const Room: React.FC<RoomProps> = ({ room }) => {
    return (
        <div className="room-box">
            <p><strong>Room {room.number}</strong></p>
            <p>Capacity: {room.capacity}</p>
            <p>Floor: {room.floor}</p>
        </div>
    );
};

export default Room;
