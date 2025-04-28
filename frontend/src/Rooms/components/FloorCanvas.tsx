import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Room from './Room';
import AddSlot from './AddSlot';
import RoomModal from './RoomModal';
import RoomEditModal from './RoomEditModal'; // nowy modal
import './floor.css';

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

type GroupedRoomsDisplay = {
    groupName: string | null;
    rooms: RoomType[];
};

const FloorCanvas: React.FC = () => {
    const [groupedRooms, setGroupedRooms] = useState<GroupedRoomsDisplay[]>([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [editModalOpen, setEditModalOpen] = useState(false);
    const [insertIndex, setInsertIndex] = useState<number>(0);
    const [selectedRoom, setSelectedRoom] = useState<RoomType | null>(null);

    const fetchRooms = async () => {
        try {
            const response = await axios.get<RoomType[]>('/api/dorm/room');
            const grouped = groupRooms(response.data);
            setGroupedRooms(grouped);
        } catch (error) {
            console.error('Error fetching rooms', error);
        }
    };

    const groupRooms = (rooms: RoomType[]): GroupedRoomsDisplay[] => {
        const map = new Map<string, GroupedRoomsDisplay>();

        rooms.forEach((room) => {
            const key = room.groupedRooms?.id.toString() || `single-${room.id}`;
            if (!map.has(key)) {
                map.set(key, {
                    groupName: room.groupedRooms?.groupName || null,
                    rooms: [],
                });
            }
            map.get(key)!.rooms.push(room);
        });

        return Array.from(map.values());
    };

    const handleOpenModal = (index: number) => {
        setInsertIndex(index);
        setModalOpen(true);
    };

    const handleAddRoom = async (newRoom: Omit<RoomType, 'id' | 'groupedRooms'>) => {
        try {
            const response = await axios.post<RoomType>('/api/dorm/room/create', newRoom);
            const updatedRooms = [...groupedRooms.flatMap(g => g.rooms), response.data];
            setGroupedRooms(groupRooms(updatedRooms));
            setModalOpen(false);
        } catch (error) {
            console.error('Error adding room', error);
        }
    };

    const handleRoomClick = (room: RoomType) => {
        setSelectedRoom(room);
        setEditModalOpen(true);
    };

    const handleUpdateRoom = async (updatedRoom: { id: string; number: string; floor: number }) => {
        try {
            await axios.patch(`/api/dorm/room/${updatedRoom.id}`, {
                name: updatedRoom.number,
                floor: updatedRoom.floor
            });
            fetchRooms();
            setEditModalOpen(false);
        } catch (error) {
            console.error('Error updating room', error);
        }
    };


    const handleDeleteRoom = async (roomId: string) => {
        try {
            await axios.delete(`/api/dorm/room/${roomId}`);
            fetchRooms();
            setEditModalOpen(false);
        } catch (error) {
            console.error('Error deleting room', error);
        }
    };

    useEffect(() => {
        fetchRooms();
    }, []);

    return (
        <div className="floor-container">
            {groupedRooms.map((group, groupIndex) => (
                <div key={groupIndex} className="room-group">
                    {group.groupName && <div className="group-name">{group.groupName}</div>}
                    {group.rooms.map((room, roomIndex) => (
                        <React.Fragment key={room.id}>
                            <div onClick={() => handleRoomClick(room)}>
                                <Room room={room} />
                            </div>
                            <AddSlot  onClick={() => handleOpenModal(roomIndex)} />
                        </React.Fragment>
                    ))}
                </div>
            ))}
            <AddSlot onClick={() => handleOpenModal(groupedRooms.length)} />
            {modalOpen && (
                <RoomModal
                    onAddRoom={handleAddRoom}
                    onClose={() => setModalOpen(false)}
                />
            )}
            {editModalOpen && selectedRoom && (
                <RoomEditModal
                    room={selectedRoom}
                    onUpdateRoom={handleUpdateRoom}
                    onDeleteRoom={handleDeleteRoom}
                    onClose={() => setEditModalOpen(false)}
                />
            )}
        </div>
    );
};

export default FloorCanvas;
