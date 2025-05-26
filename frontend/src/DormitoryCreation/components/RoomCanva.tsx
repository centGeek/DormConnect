import { useEffect, useState } from "react";

interface RoomProps {
    id: number;
    number: string;
    capacity: number;
    floor: number;
}

interface RoomCanvaProps {
    floor: number;
    onRoomAdd: (data: boolean) => void;
    onRoomEdit: (id: number, roomNumber: string) => void;
    refresh: number;
}

function RoomCanva({ floor, onRoomAdd, onRoomEdit, refresh }: RoomCanvaProps) {
    const [rooms, setRooms] = useState<RoomProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    const fetchRooms = async () => {
        try {
            setLoading(true);
            const response: Response = await fetch(`/api/dorm/room/floor/${floor}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch rooms");
            const data = await response.json();
            setRooms(data || []);
        } catch (error: unknown) {
            console.error(
                "Error fetching rooms:",
                error instanceof Error ? error.message : error
            );
        } finally {
            setLoading(false);
        }
    };

    const handleAddRoom = () => {
        onRoomAdd(true);
    };

    useEffect(() => {
        fetchRooms();
    }, [floor, refresh]);

    return (
        <div className="room-canva-container flex flex-wrap justify-center gap-3 p-3 bg-gray-200 rounded-lg shadow-lg">
            {loading ? (
                <p className="text-gray-500 text-xs">Ładowanie...</p>
            ) : (
                <>
                    {rooms.map((room) => (
                        <div
                            key={room.id}
                            className="room-card bg-white border border-gray-500 rounded-lg p-2 w-30 text-left shadow-md hover:shadow-lg transform hover:-translate-y-1 transition duration-300 cursor-pointer flex flex-col items-start gap-1"
                            onClick={() => onRoomEdit(room.id, room.number)}
                        >
                            <p className="text-sm font-bold text-gray-700">Pokój {room.number}</p>
                            <p className="text-xs text-gray-600">Pojemność: {room.capacity}</p>
                        </div>
                    ))}
                    <div
                        onClick={handleAddRoom}
                        className="add-room bg-white border border-gray-500 rounded-lg p-2 w-30 text-left shadow-md hover:shadow-lg transform hover:-translate-y-1 transition duration-300 cursor-pointer flex items-center gap-2 justify-center"
                    >
                        +
                    </div>
                </>
            )}
        </div>
    );
}

export default RoomCanva;