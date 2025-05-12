import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Room from './Room';
import AddSlot from './AddSlot';
import RoomModal from './RoomModal';
import RoomEditModal from './RoomEditModal';
import GroupRoomsModal from './GroupRoomsModal';
import './floor.css';



// ... (importy bez zmian)

type GroupedRoomsType = {
    id: number;
    groupName: string | null;
};

type RoomType = {
    id: string;
    number: string;
    capacity: number;
    floor: number;
    groupedRooms: GroupedRoomsType | null;
    startDate?: string; // ← tylko jeśli masz takie pole
};

type GroupedRoomsDisplay = {
    groupName: string | null;
    rooms: RoomType[];
};

const FloorCanvas: React.FC = () => {
    const [groupedRooms, setGroupedRooms] = useState<GroupedRoomsDisplay[]>([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [editModalOpen, setEditModalOpen] = useState(false);
    const [groupModalOpen, setGroupModalOpen] = useState(false);
    const [selectedRoom, setSelectedRoom] = useState<RoomType | null>(null);
    const [selectedRooms, setSelectedRooms] = useState<Set<string>>(new Set());

    const fetchRooms = async () => {
        try {
            const response = await axios.get<RoomType[]>('/api/dorm/room');
            const grouped = groupRooms(response.data);
            setGroupedRooms(grouped);
            setSelectedRooms(new Set());
        } catch (error) {
            console.error('Error fetching rooms', error);
        }
    };

    const groupRooms = (rooms: RoomType[]): GroupedRoomsDisplay[] => {
        const groupsMap = new Map<string, GroupedRoomsDisplay>();

        rooms.forEach((room) => {
            if (room.groupedRooms && room.groupedRooms.id !== null) {
                const groupId = room.groupedRooms.id.toString();

                if (!groupsMap.has(groupId)) {
                    groupsMap.set(groupId, {
                        groupName: room.groupedRooms.groupName || `Grupa ${groupId}`,
                        rooms: [],
                    });
                }

                groupsMap.get(groupId)!.rooms.push(room);
            } else {
                const singleKey = `single-${room.id}`;
                groupsMap.set(singleKey, {
                    groupName: null,
                    rooms: [room],
                });
            }
        });

        // Sortowanie pokoi w ramach każdej grupy po startDate (jeśli istnieje)
        const groupedArray = Array.from(groupsMap.values()).map(group => ({
            ...group,
            rooms: [...group.rooms].sort((a, b) => {
                if (!a.startDate || !b.startDate) return 0;
                return new Date(a.startDate).getTime() - new Date(b.startDate).getTime();
            }),
        }));

        return groupedArray;
    };

    const handleOpenModal = () => {
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
                floor: updatedRoom.floor,
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

    const handleGroupRooms = async (roomIds: string[], groupName: string) => {
        try {
            await axios.post('/api/dorm/room/group/create', {
                groupName: groupName,
                roomIds: roomIds,
            });
            fetchRooms();
            setGroupModalOpen(false);
        } catch (error) {
            console.error('Error grouping rooms', error);
        }
    };

    const toggleRoomSelection = (roomId: string) => {
        setSelectedRooms(prev => {
            const updated = new Set(prev);
            if (updated.has(roomId)) {
                updated.delete(roomId);
            } else {
                updated.add(roomId);
            }
            return updated;
        });
    };

    useEffect(() => {
        fetchRooms();
    }, []);

    return (
        <div className="floor-container">
            {selectedRooms.size > 0 && (
                <div style={{ width: '100%', textAlign: 'center', marginBottom: '20px' }}>
                    <button
                        className="btn-primary"
                        onClick={() => setGroupModalOpen(true)}
                    >
                        Grupuj zaznaczone pokoje ({selectedRooms.size})
                    </button>
                </div>
            )}


            {groupedRooms.map((group, groupIndex) => (
                <div key={groupIndex} className="room-group">
                    {group.groupName && <div className="group-name">{group.groupName}</div>}
                    {group.rooms.map((room) => (
                        <div key={room.id} className="room-wrapper" style={{ display: 'flex', alignItems: 'center' }}>
                            <input
                                type="checkbox"
                                checked={selectedRooms.has(room.id)}
                                onChange={() => toggleRoomSelection(room.id)}
                                style={{ marginRight: '8px' }}
                            />
                            <div onClick={() => handleRoomClick(room)} style={{ flexGrow: 1 }}>
                                <Room room={room} />
                            </div>
                        </div>
                    ))}
                </div>
            ))}

            {/* Tylko jeden przycisk dodawania pokoju */}
            <AddSlot onClick={handleOpenModal} />

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
            {groupModalOpen && (
                <GroupRoomsModal
                    onGroup={(groupName) => handleGroupRooms(Array.from(selectedRooms), groupName)}
                    onClose={() => setGroupModalOpen(false)}
                />
            )}
        </div>
    );
};

export default FloorCanvas;
