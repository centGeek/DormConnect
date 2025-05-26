import { useEffect, useState } from 'react';
import Template from '../Template/Template.tsx';
import CommonRoomCanva  from "./components/CommonRoomCanva.tsx";
import PopUpCommonRoomCreate from './components/CommonRoomPopUps/PopUpCommonRoomCreate.tsx';
import PopUpCommonRoomEdit from "./components/CommonRoomPopUps/PopUpCommonRoomEdit.tsx";
import PopUpRemoveChoice from "./components/FloorPupUps/PopUpRemoveChoice.tsx";
import PopUpRemoveFloor from "./components/FloorPupUps/PopUpRemoveFloor.tsx";
import PopUpRemoveAllRooms from "./components/FloorPupUps/PopUpRemoveAllRooms.tsx";
import RoomCanva from "./components/RoomCanva.tsx";
import PopUpRoomCreate from "./components/RoomPupUps/PopUpRoomCreate.tsx";

function CreateDormitory() {
    const [floors, setFloors] = useState<number[]>([]);
    const [loadingFloors, setLoadingFloors] = useState<boolean>(true);
    const [activeFloor, setActiveFloor] = useState<number>(-1);
    const [isPopupCRCreateOpen, setIsPopupCRCreateOpen] = useState<boolean>(false);
    const [isPopupCREditOpen, setIsPopupCREditOpen] = useState<boolean>(false);
    const [refresh_rooms_value, setRefresh_rooms_value] = useState<number>(0);
    const [refresh_floors_value, setRefresh_floors_value] = useState<number>(0);
    const [commonRoomId, setCommonRoomId] = useState<number | null>(null);
    const [isPopUpRemoveDialogOpen, setIsPopUpRemoveDialogOpen] = useState<boolean>(false);
    const [isPopUpRemoveFloorsOpen, setIsPopUpRemoveFloorsOpen] = useState<boolean>(false);
    const [isPopUpRemoveRoomsOpen, setIsPopUpRemoveRoomsOpen] = useState<boolean>(false);
    const [isPopUpRoomCreateOpen, setIsPopUpRoomCreateOpen] = useState<boolean>(false);

    const handleCommonRoomEdit = (id: number) => {
        setCommonRoomId(id);
        setIsPopupCREditOpen(true);
    };

    const handleCommonRoomAdd = (data: boolean) => {
        setIsPopupCRCreateOpen(data);
    };
    const handleRoomAdd = (data: boolean) => {
        setIsPopUpRoomCreateOpen(data);
    };
    const handleRemoveFloor = () => {
        setIsPopUpRemoveDialogOpen(false);
        // setIsPopUpRemoveFloorsOpen(true);
        alert("Na razie ta funkcjonalność jest niedostępna")
    }
    const handleClosePopup = () => {
        setIsPopupCRCreateOpen(false);
        setIsPopupCREditOpen(false);
        setIsPopUpRemoveDialogOpen(false);
        setIsPopUpRemoveFloorsOpen(false);
        setIsPopUpRemoveRoomsOpen(false);
        setIsPopUpRoomCreateOpen(false);
        setRefresh_rooms_value(refresh_rooms_value+1)
        setRefresh_floors_value(refresh_rooms_value+1)
    };
    const handleRemoveRooms = () => {
        setIsPopUpRemoveDialogOpen(false);
        setIsPopUpRemoveRoomsOpen(true);
    };

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
            <div className="relative min-h-fit">
                <h1 className="text-4xl font-bold text-gray-600 mb-6 text-center">Kreator akademika</h1>
                {!(activeFloor===-1)&&(<button
                    onClick={() => setIsPopUpRemoveDialogOpen(true)}
                    className="absolute top-4 right-4 bg-red-500 text-white w-12 h-12 rounded-full flex items-center justify-center text-xl font-bold shadow hover:bg-red-600 transition"
                >
                    ×
                </button>)}
            </div>
            {!loadingFloors && (
                <div className="flex h-fit justify-start">
                    <div className="w-1/6 bg-gray-200 p-4 flex flex-col items-center justify-center">
                        <h1 className="mt-4 text-center text-2xl font-extrabold text-gray-700">Piętra</h1>
                        <button
                            onClick={() => {
                                fetch('/api/floors/add', { method: 'POST', credentials: "include" });
                                setRefresh_floors_value(refresh_floors_value + 1);
                            }}
                            className="w-8 h-8 bg-gray-500 text-white rounded-full flex items-center justify-center text-xl hover:bg-gray-600 transition"
                        >
                            +
                        </button>
                        <div className="mt-4 w-full h-96 overflow-y-auto flex flex-col-reverse gap-4 items-center">
                            {floors.map((floor, index) => (
                                <div
                                    key={index}
                                    onClick={() => setActiveFloor(floor)}
                                    className={`w-32 h-32 rounded-lg flex items-center justify-center text-xl font-bold cursor-pointer transition ${
                                        activeFloor === floor
                                            ? 'bg-gray-500 text-white shadow-lg'
                                            : 'bg-white shadow-md hover:bg-gray-300'
                                    }`}
                                >
                                    {floor}
                                </div>
                            ))}
                        </div>
                    </div>

                    {!(activeFloor === -1) && (
                        <div>
                            <div>
                                <h3>Pokoje</h3>
                                <RoomCanva
                                    floor={activeFloor}
                                    onRoomAdd={ handleRoomAdd}
                                    onRoomEdit={(id) => console.log("Edytuj pokój:", id)}
                                    refresh={refresh_rooms_value}
                                />
                            </div>
                            <div>
                                <h3>Pokoje wspólne</h3>
                                <CommonRoomCanva
                                    floor={activeFloor}
                                    onCommonRoomAdd={handleCommonRoomAdd}
                                    onCommonRoomEdit={handleCommonRoomEdit}
                                    refresh={refresh_rooms_value}
                                />
                            </div>
                        </div>
                    )}
                </div>
            )}
            {isPopupCRCreateOpen && (
                <PopUpCommonRoomCreate onClose={handleClosePopup} floor={activeFloor} />
            )}
            {isPopupCREditOpen && commonRoomId !== null && (
                <PopUpCommonRoomEdit onClose={handleClosePopup} common_room_id={commonRoomId} />
            )}
            {isPopUpRemoveDialogOpen && (
                <PopUpRemoveChoice onClose={handleClosePopup} onRemoveFloor={handleRemoveFloor} onRemoveRooms={handleRemoveRooms}/>
            )}
            {isPopUpRemoveFloorsOpen && (
                <PopUpRemoveFloor onClose={handleClosePopup} floor={activeFloor}/>
            )}
            {isPopUpRemoveRoomsOpen && (
                <PopUpRemoveAllRooms onClose={handleClosePopup} floor={activeFloor}/>
            )}
            {isPopUpRoomCreateOpen && (
                <PopUpRoomCreate onClose={handleClosePopup} floor={activeFloor}/>
            )}
        </Template>
    );
}

export default CreateDormitory;