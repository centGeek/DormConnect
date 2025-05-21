import { useEffect, useState } from 'react';
import Template from '../Template/Template.tsx';
import CommonRoomCanva  from "./components/CommonRoomCanva.tsx";
import PopUpCommonRoomCreate from './components/PopUpCommonRoomCreate.tsx';

function CreateDormitory() {
    const [floors, setFloors] = useState<number[]>([]);
    const [loadingFloors, setLoadingFloors] = useState<boolean>(true);
    const [activeFloor, setActiveFloor] = useState<number>(-1);
    const [isPopupOpen, setIsPopupOpen] = useState<boolean>(false); // Dodano stan
    const [refresh_rooms_value, setRefresh_rooms_value] = useState<number>(0);
    const [refresh_floors_value, setRefresh_floors_value] = useState<number>(0);

    const handleCommonRoomAdd = (data: boolean) => {
        setIsPopupOpen(data);

    };

    const handleClosePopup = () => {
        setIsPopupOpen(false);
        setRefresh_rooms_value(refresh_rooms_value+1)
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
            <div className="min-h-fit">
                <h1 className="text-4xl font-bold text-gray-600 mb-6 text-center">Kreator akademika</h1>
            </div>
            {!loadingFloors && (
                <div className="flex h-fit justify-start">
                    <div className="w-1/6 bg-gray-200 p-4 flex flex-col items-center justify-center">
                        <h1 className="mt-4 text-center text-2xl font-extrabold text-gray-700">Piętra</h1>
                        <button
                            onClick={() => fetch('/api/floors/add', { method: 'POST', credentials: "include" })}
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
                    <div>
                        <div>
                            <h3>Pokoje wspólne</h3>
                            <CommonRoomCanva floor={activeFloor} onCommonRoomAdd={handleCommonRoomAdd} refresh={refresh_rooms_value}/>
                        </div>
                    </div>
                </div>
            )}
            {isPopupOpen && (
                <PopUpCommonRoomCreate onClose={handleClosePopup} floor={activeFloor} />
            )}
        </Template>
    );
}

export default CreateDormitory;