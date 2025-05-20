import { useEffect, useState } from "react";
import CommonRoomTypes from "../../ReusableComponents/CommonRoomTypes.tsx";
import CommonRoomIcon from "../../ReusableComponents/CommonRoomIcon.tsx";

interface CommonRoomProps {
    id: number;
    type: string;
    floor: number;
    capacity: number;
    timesAWeekYouCanUseIt: number;
    isArchived: boolean;
}

interface CommonRoomCanvaProps {
    floor: number; // Zmienna przekazywana jako props
}

function CommonRoomCanva({ floor }: CommonRoomCanvaProps) {
    const [commonRooms, setCommonRooms] = useState<CommonRoomProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    const fetchCommonRooms = async () => {
        try {
            setLoading(true);
            const response: Response = await fetch(`/api/common-room/show/${floor}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch common rooms");
            const data = await response.json();
            setCommonRooms(data || []);
        } catch (error: unknown) {
            console.error(
                "Error fetching common rooms:",
                error instanceof Error ? error.message : error
            );
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (floor !== -1) {
            fetchCommonRooms(); // Wywołanie fetch przy zmianie floor
        }
    }, [floor]);

    return (
        <div className="common-room-canva-container flex flex-wrap justify-center gap-6 p-6 bg-gray-200 rounded-lg shadow-lg">
            {loading ? (
                <p className="text-gray-500">Ładowanie...</p>
            ) : commonRooms.length === 0 ? (
                <p className="text-gray-500">Nie znaleziono żadnych pokoi</p>
            ) : (
                commonRooms.map((commonRoom) => (
                    <div
                        key={commonRoom.id}
                        className="common-room-card bg-white border border-gray-500 rounded-lg p-4 w-72 text-center shadow-md hover:shadow-lg transform hover:-translate-y-1 transition duration-300 cursor-pointer"
                    >
                        <h2 className="text-lg font-bold text-gray-700 mb-2">
                            {CommonRoomTypes(commonRoom.type)}
                        </h2>
                        <p className="text-gray-600">Pojemność: {commonRoom.capacity}</p>
                        {commonRoom.isArchived && (
                            <p className="text-red-500 font-bold">Zarchiwizowany</p>
                        )}
                    </div>
                ))
            )
            }
        </div>
    );
}

export default CommonRoomCanva;