import {useEffect, useRef, useState} from 'react';
import Template from '../Template/Template.tsx';
import CommonRoomCanva from "./components/CommonRoomCanva.tsx";
import PopUpCommonRoomCreate from './components/CommonRoomPopUps/PopUpCommonRoomCreate.tsx';
import PopUpCommonRoomEdit from "./components/CommonRoomPopUps/PopUpCommonRoomEdit.tsx";
import PopUpRemoveChoice from "./components/FloorPupUps/PopUpRemoveChoice.tsx";
import PopUpRemoveFloor from "./components/FloorPupUps/PopUpRemoveFloor.tsx";
import PopUpRemoveAllRooms from "./components/FloorPupUps/PopUpRemoveAllRooms.tsx";
import RoomCanva from "./components/RoomCanva.tsx";
import PopUpRoomCreate from "./components/RoomPupUps/PopUpRoomCreate.tsx";
import PopUpRoomDelete from "./components/RoomPupUps/PopUpRoomDelete.tsx";
import OperationSuccedPopUp from "./components/OperationSuccedPopUp.tsx";
import PopUpRemoveFailed from "./components/FloorPupUps/PopUpRemoveFailed.tsx";
import Cookies from "js-cookie";
import {useNavigate} from 'react-router-dom';

function CreateDormitory() {
    const [floors, setFloors] = useState<number[]>([]);
    const [loadingFloors, setLoadingFloors] = useState<boolean>(true);
    const [activeFloor, setActiveFloor] = useState<number>(-1);
    const [isPopupCRCreateOpen, setIsPopupCRCreateOpen] = useState<boolean>(false);
    const [isPopupCREditOpen, setIsPopupCREditOpen] = useState<boolean>(false);
    const [refresh_rooms_value, setRefresh_rooms_value] = useState<number>(0);
    const [refresh_floors_value, setRefresh_floors_value] = useState<number>(0);
    const [commonRoomId, setCommonRoomId] = useState<number | null>(null);
    const [roomId, setRoomId] = useState<number | null>(null);
    const [roomNumber, setRoomNumber] = useState<string | null>(null);
    const [isPopUpRemoveDialogOpen, setIsPopUpRemoveDialogOpen] = useState<boolean>(false);
    const [isPopUpRemoveFloorsOpen, setIsPopUpRemoveFloorsOpen] = useState<boolean>(false);
    const [isPopUpRemoveRoomsOpen, setIsPopUpRemoveRoomsOpen] = useState<boolean>(false);
    const [isPopUpRoomCreateOpen, setIsPopUpRoomCreateOpen] = useState<boolean>(false);
    const [isPopUpRemoveRoomOpen, setIsPopUpRemoveRoomOpen] = useState<boolean>(false);
    const [isPopUpSuccedOpen, setIsPopUpSuccedOpen] = useState<boolean>(false);
    const [isPopUpRemoveFailedOpen, setIsPopUpRemoveFailedOpen] = useState<boolean>(false);
    const navigate = useNavigate();

    const floorsContainerRef = useRef<HTMLDivElement>(null);

    const handleCommonRoomEdit = (id: number) => {
        setCommonRoomId(id);
        setIsPopupCREditOpen(true);
    };
    const handleRoomEdit = (id: number, roomNumber: string) => {
        setRoomId(id);
        setRoomNumber(roomNumber);
        setIsPopUpRemoveRoomOpen(true);
    }
    const handleCommonRoomAdd = (data: boolean) => {
        setIsPopupCRCreateOpen(data);
    };
    const handleRoomAdd = (data: boolean) => {
        setIsPopUpRoomCreateOpen(data);
    };
    const handleRemoveFloor = () => {
        setIsPopUpRemoveDialogOpen(false);
        setIsPopUpRemoveFloorsOpen(true);
    };
    const handleClosePopup = () => {
        setIsPopupCRCreateOpen(false);
        setIsPopupCREditOpen(false);
        setIsPopUpRemoveDialogOpen(false);
        setIsPopUpRemoveFloorsOpen(false);
        setIsPopUpRemoveRoomsOpen(false);
        setIsPopUpRoomCreateOpen(false);
        setIsPopUpRemoveRoomOpen(false);
        setIsPopUpSuccedOpen(false);
        setIsPopUpRemoveFailedOpen(false);
        setRefresh_rooms_value(refresh_rooms_value + 1)
        setRefresh_floors_value(refresh_rooms_value + 1)
    };
    const handleRemoveRooms = () => {
        setIsPopUpRemoveDialogOpen(false);
        setIsPopUpRemoveRoomsOpen(true);
    };
    const getFloors = async () => {
        try {
            setLoadingFloors(true);
            const response = await fetch('/api/floors/get', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('token')}`,
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

    useEffect(() => {
        getFloors();
    }, [refresh_floors_value]);

    const handleAddFloor = async () => {
        try {
            await fetch('/api/floors/add', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('token')}`,
                },
            });
            setRefresh_floors_value(refresh_floors_value + 1);

            if (floorsContainerRef.current) {
                floorsContainerRef.current.scrollTo({top: 0, behavior: 'smooth'});
            }
        } catch (error) {
            console.error('Błąd podczas dodawania piętra:', error);
        }
    };

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
            <div className="w-full md:w-1/4 flex justify-center items-start p-5">
                <button
                    type="button"
                    className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                    onClick={() => navigate(-1)}
                >
                    ← Powrót
                </button>
            </div>
            <div className="relative min-h-fit">
                <h1 className="text-4xl font-bold text-gray-600 mb-6 text-center">Kreator akademika</h1>
                {!(activeFloor === -1) && (
                    <button
                        onClick={() => setIsPopUpRemoveDialogOpen(true)}
                        className="absolute top-4 right-4 bg-red-500 text-white w-12 h-12 rounded-full flex items-center justify-center text-xl font-bold shadow hover:bg-red-600 transition"
                    >
                        ×
                    </button>
                )}
            </div>
            {!loadingFloors && (
                <div className="flex flex-wrap md:flex-nowrap justify-start gap-4">
                    <div className="w-full md:w-1/6 bg-gray-200 p-4 flex flex-col items-center justify-center rounded-xl">
                        <h1 className="mt-4 text-center text-2xl font-extrabold text-gray-700">Piętra</h1>
                        <button
                            onClick={handleAddFloor}
                            className="w-8 h-8 m-2 bg-gray-500 text-white rounded-full flex items-center justify-center text-xl hover:bg-gray-600 transition"
                        >
                            +
                        </button>
                        <div
                            ref={floorsContainerRef}
                            className="mt-2 w-full h-96 overflow-y-auto flex flex-col-reverse gap-4 items-center"
                        >
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
                        <div className="w-full md:w-5/6 flex flex-col gap-4">
                            <div>
                                <h3 className="text-lg font-bold mb-2">Pokoje</h3>
                                <RoomCanva
                                    floor={activeFloor}
                                    onRoomAdd={handleRoomAdd}
                                    onRoomEdit={handleRoomEdit}
                                    refresh={refresh_rooms_value}
                                />
                            </div>
                            <div>
                                <h3 className="text-lg font-bold mb-2">Pokoje wspólne</h3>
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
                <PopUpCommonRoomCreate onClose={handleClosePopup} floor={activeFloor}
                                       onSucced={() => setIsPopUpSuccedOpen(true)}/>
            )}
            {isPopupCREditOpen && commonRoomId !== null && (
                <PopUpCommonRoomEdit onClose={handleClosePopup} common_room_id={commonRoomId}
                                     onSucced={() => setIsPopUpSuccedOpen(true)}/>
            )}
            {isPopUpRemoveDialogOpen && (
                <PopUpRemoveChoice onClose={handleClosePopup} onRemoveFloor={handleRemoveFloor}
                                   onRemoveRooms={handleRemoveRooms}/>
            )}
            {isPopUpRemoveFloorsOpen && (
                <PopUpRemoveFloor onClose={handleClosePopup} floor={activeFloor} onSucess={() => setIsPopUpSuccedOpen(true)} failed={() => setIsPopUpRemoveFailedOpen(true)}/>
            )}
            {isPopUpRemoveRoomsOpen && (
                <PopUpRemoveAllRooms onClose={handleClosePopup} floor={activeFloor}/>
            )}
            {isPopUpRoomCreateOpen && (
                <PopUpRoomCreate onClose={handleClosePopup} floor={activeFloor}
                                 onSucced={() => setIsPopUpSuccedOpen(true)}/>
            )}
            {isPopUpRemoveRoomOpen && (
                <PopUpRoomDelete onClose={handleClosePopup} roomNumber={roomNumber ?? ''} roomId={roomId ?? -1}/>
            )}
            {isPopUpSuccedOpen && (
                <OperationSuccedPopUp onClose={handleClosePopup}/>
            )}
            {isPopUpRemoveFailedOpen && (
                <PopUpRemoveFailed onClose={() =>setIsPopUpRemoveFailedOpen(false)}/>
            )}
        </Template>
    );
}

export default CreateDormitory;