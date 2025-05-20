import { useEffect, useState } from 'react';
import Template from '../Template/Template.tsx';
import CommonRoomCanva  from "./components/CommonRoomCanva.tsx";

function CreateDormitory() {
    const [floors, setFloors] = useState<number[]>([]);
    const [loadingFloors, setLoadingFloors] = useState<boolean>(true);
    const [activeFloor, setActiveFloor] = useState<number>(-1);

    const addFloor = () => {
        try {
            fetch('/api/floors/add', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include"
            });
        } catch (error) {
            console.error('Error adding floor:', error);
        }
    };

    const handleFloorClick = (floor: number) => {
        setActiveFloor(floor);
    };

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

    useEffect(() => {
        getFloors();
    }, []);

    return (
        <Template buttons={[{ text: 'Home', link: '/home' }, { text: 'Rooms', link: '/rooms' }]}>
            <div className="min-h-fit">
                <h1 className="text-4xl font-bold text-gray-600 mb-6 text-center">Kreator akademika</h1>
            </div>
            {!loadingFloors && (
                <div className="flex h-fit justify-start">
                    {/* Panel z lewej strony */}
                    <div className="w-1/6 bg-gray-200 p-4 flex flex-col items-center justify-center">
                        <h1 className="mt-4 text-center text-2xl font-extrabold text-gray-700">Piętra</h1>
                        <button
                            onClick={addFloor}
                            className="w-8 h-8 bg-gray-500 text-white rounded-full flex items-center justify-center text-xl hover:bg-gray-600 transition"
                        >
                            +
                        </button>
                        <div className="mt-4 w-full h-96 overflow-y-auto flex flex-col-reverse gap-4 items-center">
                            {floors.map((floor, index) => (
                                <div
                                    key={index}
                                    onClick={() => handleFloorClick(floor)}
                                    className="w-32 h-32 bg-white shadow-md rounded-lg flex items-center justify-center text-xl font-bold cursor-pointer hover:bg-gray-300 transition"
                                >
                                    {floor}
                                </div>
                            ))}
                        </div>
                    </div>
                    {/*Widok pokojów*/}
                    <div>
                        {/*Widok pokojów mieszkalnych*/}
                        <div>

                        </div>
                        {/*Widok pokojów wspólnych*/}
                        <div>
                            <h3>Pokoje wspólne</h3>
                            <CommonRoomCanva floor={activeFloor}/>
                        </div>
                    </div>
                </div>
            )}
        </Template>
    );
};

export default CreateDormitory;