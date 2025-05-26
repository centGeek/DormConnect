import { useEffect, useState } from 'react';
import Template from '../Template/Template.tsx';
import CommonRoomCanva  from "./components/CommonRoomCanva.tsx";
import PopUpCommonRoomCreate from './components/PopUpCommonRoomCreate.tsx';
import PopUpCommonRoomEdit from "./components/PopUpCommonRoomEdit.tsx";

function CreateDormitory() {
    const [floors, setFloors] = useState<number[]>([]);
    const [loadingFloors, setLoadingFloors] = useState<boolean>(true);
    const [activeFloor, setActiveFloor] = useState<number>(-1);
    const [isPopupCRCreateOpen, setisPopupCRCreateOpen] = useState<boolean>(false);
    const [isPopupCREditOpen, setisPopupCREditOpen] = useState<boolean>(false);
    const [refresh_rooms_value, setRefresh_rooms_value] = useState<number>(0);
    const [refresh_floors_value, setRefresh_floors_value] = useState<number>(0);
    const [commonRoomId, setCommonRoomId] = useState<number | null>(null);


    const handleCommonRoomEdit = (id: number) => {
        setCommonRoomId(id);
        setisPopupCREditOpen(true);
    };

    const handleCommonRoomAdd = (data: boolean) => {
        setisPopupCRCreateOpen(data);
    };

    const handleClosePopup = () => {
        setisPopupCRCreateOpen(false);
        setisPopupCREditOpen(false);
        setRefresh_rooms_value(refresh_rooms_value+1)
    };
    const handleFloorAdd = async  () => {
        try {
            const response = await fetch('/api/floors/add', {method: 'POST', credentials: "include"});
            if(!response.ok) {
                throw new Error("Failed to add floor");
            }
            setRefresh_floors_value(refresh_floors_value+1);
        }
        catch (error) {
            console.error('Error adding floor:', error instanceof Error ? error.message : error);
        }
    };

    const handleFloorDelete = async (floor: number) => {
        try {
            const response = await fetch(`/api/floors/delete/${floor}`, {method: 'DELETE', credentials: "include"});
            if(!response.ok) {
                throw new Error("Failed to delete floor");
            }
            setRefresh_floors_value(refresh_floors_value+1);
        }
        catch (error) {
            console.error('Error deleting floor:', error instanceof Error ? error.message : error);
        }
    }


    useEffect(() => {
        const getFloors = async () => {
            try {
                setLoadingFloors(true);
                const response = await fetch('/api/floors/get', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: "include"
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch floors");
                }

                const data = (await response.json()) as number[];
                setFloors(data);
            } catch (error: unknown) {
                console.error(
                    'Error fetching floors:',
                    error instanceof Error ? error.message : error
                );
            } finally {
                setLoadingFloors(false);
            }
        };

        getFloors();
    }, [refresh_floors_value]);

    return (
        <Template buttons={[{ text: 'Home', link: '/home' }, { text: 'Rooms', link: '/rooms' }]}>
            <div className="min-h-fit">
                <h1 className="text-4xl font-bold text-gray-600 mb-6 text-center">Kreator akademika</h1>
            </div>
            {!loadingFloors && (
                <div className="flex h-fit justify-start">
                    <div className="w-1/6 bg-gray-200 p-4 flex flex-col items-center justify-center">
                        <h1 className="mt-4 text-center text-2xl font-extrabold text-gray-700">Piętra</h1>
                        <button
                            onClick={() => handleFloorAdd()}
                            className="w-8 h-8 bg-gray-500 text-white rounded-full flex items-center justify-center text-xl hover:bg-gray-600 transition"
                        >
                            +
                        </button>
                        <div className="mt-4 w-full h-96 overflow-y-auto flex flex-col-reverse gap-4 items-center">
                            {floors.map((floor, index) => (
                                <div
                                    key={index}
                                    className={`relative w-32 h-32 rounded-lg flex items-center justify-center text-xl font-bold cursor-pointer transition ${
                                        activeFloor === floor
                                            ? 'bg-gray-500 text-white shadow-lg'
                                            : 'bg-white shadow-md hover:bg-gray-300'
                                    }`}
                                >
                                    <span onClick={() => setActiveFloor(floor)}>{floor}</span>
                                    <button
                                        onClick={() => handleFloorDelete(floor)}
                                        className="absolute top-1 right-1 w-5 h-5 bg-red-500 text-white rounded-full flex items-center justify-center text-sm hover:bg-red-600 transition"
                                    >
                                        ✕
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div>
                        <div>
                            <h3>Pokoje wspólne</h3>
                            <CommonRoomCanva floor={activeFloor} onCommonRoomAdd={handleCommonRoomAdd}  onCommonRoomEdit={handleCommonRoomEdit} refresh={refresh_rooms_value}/>
                        </div>
                    </div>
                </div>
            )}
            {isPopupCRCreateOpen && (
                <PopUpCommonRoomCreate onClose={handleClosePopup} floor={activeFloor} />
            )}
            {isPopupCREditOpen && commonRoomId !== null && (
                <PopUpCommonRoomEdit onClose={handleClosePopup} common_room_id={commonRoomId} />
            )}
        </Template>
    );
}

export default CreateDormitory;