import { useEffect, useState } from "react"
import getRoomStatusTranslation from "../../ReusableComponents/CommonRoomTypes";
import CommonRoomTypes from "../../ReusableComponents/CommonRoomTypes";
import getRoomIcon from "../../ReusableComponents/CommonRoomIcon";

interface CommonRoomProps {
    id: number;
    commonRoomType: string;
    floor: number;
    capacity: number;
    timesAWeekYouCanUseIt: number;
    isArchived: boolean;
}

interface CommonRoomCanvaProps {
    floor: number;
    onCommonRoomAdd: (data: boolean) => void;
    onCommonRoomEdit: (id:number) => void;
    refresh: number;
}

function CommonRoomCanva({ floor, onCommonRoomAdd, onCommonRoomEdit, refresh }: CommonRoomCanvaProps) {
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

    const handleAddSpecialRoom = () => {
        onCommonRoomAdd(true);
    };


    useEffect(() => {
        fetchCommonRooms();
    }, [floor, refresh]);

    return (
        <div className="common-room-canva-container flex flex-wrap justify-center gap-3 p-3 bg-gray-200 rounded-lg shadow-lg">
            {loading ? (
                <p className="text-gray-500 text-xs">Ładowanie...</p>
            ) : (
                <>
                    {commonRooms.map((commonRoom) => (
                        <div
                            key={commonRoom.id}
                            className="common-room-card bg-white border border-gray-500 rounded-lg p-2 w-30 text-left shadow-md hover:shadow-lg transform hover:-translate-y-1 transition duration-300 cursor-pointer flex items-center gap-2"
                            onClick={() => onCommonRoomEdit(commonRoom.id)}
                        >
                            <img
                                src={`/src/assets/common_room_icons/${getRoomIcon(commonRoom.commonRoomType)}.png`}
                                alt={getRoomStatusTranslation(commonRoom.commonRoomType)}
                                className="h-6 w-6"
                            />
                            <p className="text-xs font-bold text-gray-700">
                                {CommonRoomTypes(commonRoom.commonRoomType)}
                            </p>
                        </div>
                    ))}
                    <div
                        onClick={handleAddSpecialRoom}
                        className="add-special-room bg-white border border-gray-500 rounded-lg p-2 w-30 text-left shadow-md hover:shadow-lg transform hover:-translate-y-1 transition duration-300 cursor-pointer flex items-center gap-2 justify-center"
                    >
                        +
                    </div>
                </>
            )}
        </div>
    );
}

export default CommonRoomCanva;