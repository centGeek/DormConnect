import React, {useState} from 'react';
import Template from '../Template/Template';
import {parseJwt} from '../JWT/JWTDecoder';
import {MapContainer, TileLayer, Marker, useMapEvents} from 'react-leaflet';
import L from 'leaflet';
import {Power} from "lucide-react";

function LocationPicker({onLocationSelect}: { onLocationSelect: (latlng: L.LatLng) => void }) {
    useMapEvents({
        click(e) {
            onLocationSelect(e.latlng);
        },
    });
    return null;
}

function DormFormPage() {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [comments, setComments] = useState('');
    const [income, setIncome] = useState<number | ''>('');
    const [priorityScore, setPriorityScore] = useState<number | ''>('');
    const [selectedLatLng, setSelectedLatLng] = useState<L.LatLng | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);

    const poliCorrinates = L.latLng(51.74899574307592, 19.45339553079945);

    const calculatePriority = (latlng: L.LatLng, income: number): number => {
        const distance = latlng.distanceTo(poliCorrinates) / 1000; // w km
        const incomePoints = (Math.max(0, Math.min(3500, (2500 / (income * 0.001))) - 600) / 100 * 3);
        const distancePoints = Math.max(0, distance - 40);
        let distancePointsAdjusted;
        if (distancePoints > 100) {
            distancePointsAdjusted = 100 + Math.sqrt(distancePoints);
        } else distancePointsAdjusted = distancePoints;

        const priorityScore = Math.round((incomePoints + distancePointsAdjusted) / 2);
        console.log(priorityScore, distance, distancePoints, incomePoints);
        return priorityScore;
    };

    const handleLocationSelect = (latlng: L.LatLng) => {
        setSelectedLatLng(latlng);
        if (income && income > 0) {
            setPriorityScore(calculatePriority(latlng, income));
        }
    };

    const handleIncomeChange = (value: string) => {
        const parsed = Number(value);
        if (!isNaN(parsed)) {
            setIncome(parsed);
            if (selectedLatLng) {
                setPriorityScore(calculatePriority(selectedLatLng, parsed));
            }
        } else {
            setIncome('');
            setPriorityScore('');
        }
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        setError(null);
        setSuccessMessage(null);

        const token = document.cookie
            .split('; ')
            .find(row => row.startsWith('token='))
            ?.split('=')[1];

        if (!token) {
            setError('Brak tokena autoryzacyjnego');
            return;
        }

        const user = parseJwt(token);
        const userId = user?.id;

        if (!income) {
            setError('Ustaw wartość dochodu!');
            return;
        }

        if (!userId) {
            setError('Nieprawidłowy token użytkownika');
            return;
        }

        if (!selectedLatLng) {
            setError('Zaznacz lokalizację na mapie' + selectedLatLng + priorityScore);
            return;
        }

        const dormForm = {
            startDate,
            endDate: endDate || null,
            comments,
            priorityScore,
            isProcessed: false
        };

        try {
            const response = await fetch('/api/dorm/form', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(dormForm),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Nie udało się złożyć formularza');
            }

            setSuccessMessage('Formularz został pomyślnie złożony! Otrzymany wynik rekrutacji to: ' + priorityScore);
            setStartDate('');
            setEndDate('');
            setComments('');
            setIncome('');
            setPriorityScore('');
            setSelectedLatLng(null);
        } catch (err: any) {
            setError(err.message || 'Wystąpił błąd');
        }
    };

    return (
        <Template
            buttons={[{text: 'Chat', link: '/chat'}, {text: 'Akademiki', link: '/dorms'}]}
            footerContent={<p></p>}
        >
            <div className="flex-1 flex justify-center items-start p-5">
                <button
                    type="button"
                    className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                    onClick={() => window.history.back()}
                >
                    ← Powrót
                </button>
            </div>

            <div className="flex-3 flex justify-center items-start p-5">
                <div className="w-full max-w-lg bg-gray-100 p-5 rounded-lg shadow-md">
                    <h2 className="text-2xl font-semibold text-gray-600 text-center mb-4">
                        Złóż wniosek o akademik
                    </h2>

                    {error && <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4">{error}</div>}
                    {successMessage &&
                        <div className="bg-green-100 text-gray-600 p-3 rounded-lg mb-4">{successMessage}</div>}

                    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                        <div>
                            <label htmlFor="startDate" className="block text-gray-700 mb-1">Data rozpoczęcia</label>
                            <input
                                type="date"
                                id="startDate"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                                required
                                className="w-full p-2 border border-gray-300 rounded-lg"
                            />
                        </div>

                        <div>
                            <label htmlFor="endDate" className="block text-gray-700 mb-1">Data zakończenia
                                (opcjonalna)</label>
                            <input
                                type="date"
                                id="endDate"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                className="w-full p-2 border border-gray-300 rounded-lg"
                            />
                        </div>

                        <div>
                            <label htmlFor="income" className="block text-gray-700 mb-1">Dochód miesięczny (netto) na
                                osobę w gospodarstwie <br/> domowym (PLN)</label>
                            <input
                                type="number"
                                id="income"
                                min={0}
                                value={income}
                                onChange={(e) => handleIncomeChange(e.target.value)}
                                className="w-full p-2 border border-gray-300 rounded-lg"
                            />
                        </div>

                        <div>
                            <label className="block text-gray-700 mb-1">Zaznacz dokładnie swoje miejsce
                                zamieszkania:</label>
                            <MapContainer
                                center={[51.74899574307592, 19.45339553079945]}
                                zoom={10}
                                scrollWheelZoom
                                style={{height: '300px', width: '100%'}}
                            >
                                <TileLayer
                                    attribution='&copy; <a href="https://carto.com/">CARTO</a>'
                                    url="https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png"
                                />

                                {selectedLatLng && <Marker position={selectedLatLng}/>}
                                <LocationPicker onLocationSelect={handleLocationSelect}/>
                            </MapContainer>
                        </div>

                        <div>
                            <label htmlFor="comments" className="block text-gray-700 mb-1">Komentarze</label>
                            <textarea
                                id="comments"
                                value={comments}
                                onChange={(e) => setComments(e.target.value)}
                                className="w-full p-2 border border-gray-300 rounded-lg"
                            />
                        </div>

                        <button
                            type="submit"
                            className="bg-gray-700 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition"
                        >
                            Złóż formularz
                        </button>
                    </form>
                </div>
            </div>
        </Template>
    );
}

export default DormFormPage;
