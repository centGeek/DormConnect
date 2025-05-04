import React, { useState } from 'react';

type GroupedRoomsType = {
    id: number;
    groupName: string;
};

type RoomType = {
    id: string;
    number: string;
    capacity: number;
    floor: number;
    groupedRooms: GroupedRoomsType | null;
};

interface RoomEditModalProps {
    room: RoomType;
    onUpdateRoom: (updatedRoom: { id: string; number: string; floor: number }) => Promise<void>;
    onDeleteRoom: (roomId: string) => Promise<void>;
    onClose: () => void;
}

const RoomEditModal: React.FC<RoomEditModalProps> = ({ room, onUpdateRoom, onDeleteRoom, onClose }) => {
    const [number, setNumber] = useState(room.number);
    const [floor, setFloor] = useState(room.floor);
    const [confirmDelete, setConfirmDelete] = useState(false);

    const handleUpdate = () => {
        onUpdateRoom({
            id: room.id,
            number,
            floor
        });
    };

    const handleDeleteClick = () => {
        if (!confirmDelete) {
            setConfirmDelete(true);
        } else {
            onDeleteRoom(room.id);
        }
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2 style={{ marginBottom: '20px' }}>Edytuj pokój</h2>

                <div className="modal-form">
                    <div>
                        <label>Numer pokoju</label>
                        <input
                            type="text"
                            value={number}
                            onChange={(e) => setNumber(e.target.value)}
                            placeholder="Wpisz numer pokoju"
                        />
                    </div>

                    <div>
                        <label>Piętro</label>
                        <input
                            type="number"
                            value={floor}
                            onChange={(e) => setFloor(Number(e.target.value))}
                            placeholder="Wpisz piętro"
                        />
                    </div>
                </div>

                <div className="modal-buttons">
                    <button className="btn-primary" onClick={handleUpdate}>Zapisz</button>

                    <button
                        className={confirmDelete ? "btn-danger" : "btn-secondary"}
                        onClick={handleDeleteClick}
                    >
                        {confirmDelete ? "Potwierdź usunięcie" : "Usuń"}
                    </button>

                    <button className="btn-secondary" onClick={onClose}>Anuluj</button>
                </div>
            </div>
        </div>
    );
};

export default RoomEditModal;
