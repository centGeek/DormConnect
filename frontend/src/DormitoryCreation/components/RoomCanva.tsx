

interface RoomProps {
    id: string;
    number: string;
    capacity: number;
    floor: number;
    groupedRooms: GroupedRoomsType | null;
}