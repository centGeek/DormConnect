import React, {useState} from 'react';
import Template from '../Template/Template';
import {parseJwt} from '../JWT/JWTDecoder';
import {MapContainer, TileLayer, Marker, useMapEvents} from 'react-leaflet';
import L, {LatLng} from 'leaflet';
import {useMap} from 'react-leaflet';

function LocationPicker({onLocationSelect}: { onLocationSelect: (latlng: LatLng) => void }) {
    useMapEvents({
        click(e) {
            onLocationSelect(e.latlng);
        },
    });
    return null;
}

function MapCenterer({latlng}: { latlng: LatLng }) {
    const map = useMap();
    React.useEffect(() => {
        if (latlng) {
            map.setView(latlng, map.getZoom());
        }
    }, [latlng, map]);
    return null;
}

function DormFormPage() {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [comments, setComments] = useState('');
    const [income, setIncome] = useState<number | ''>('');
    const [priorityScore, setPriorityScore] = useState<number | ''>('');
    const [selectedLatLng, setSelectedLatLng] = useState<LatLng | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [searchQuery, setSearchQuery] = useState('');

    const poliCorrinates = L.latLng(51.74899574307592, 19.45339553079945);

    const today = new Date().toISOString().split('T')[0]; // dzisiejsza data YYYY-MM-DD

    const calculatePriority = (latlng: LatLng, income: number): number => {
        const distance = latlng.distanceTo(poliCorrinates) / 1000;
        const incomePoints = (Math.max(0, Math.min(3500, (2500 / (income * 0.001))) - 600) / 100 * 3);
        const distancePoints = Math.max(0, distance - 40);
        const adjusted = distancePoints > 100 ? 100 + Math.sqrt(distancePoints) : distancePoints;
        return Math.round((incomePoints + adjusted) / 2);
    };

    const handleLocationSelect = (latlng: LatLng) => {
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

    const geocodeLocation = async (query: string): Promise<LatLng | null> => {
        const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}`;
        const res = await fetch(url);
        const data = await res.json();
        if (data && data.length > 0) {
            setError(null);
            return L.latLng(parseFloat(data[0].lat), parseFloat(data[0].lon));
        }
        return null;
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        setSuccessMessage(null);

        const token = document.cookie.split('; ').find(row => row.startsWith('token='))?.split('=')[1];
        if (!token) return setError('Brak tokena autoryzacyjnego');

        const user = parseJwt(token);
        const userId = user?.id;
        if (!income) return setError('Ustaw wartość dochodu!');
        if (!userId) return setError('Nieprawidłowy token użytkownika');
        if (!selectedLatLng) return setError('Zaznacz lokalizację na mapie!');

        if (endDate && endDate < startDate) return setError('Data zakończenia musi być po dacie rozpoczęcia');

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

            if (!response.ok) throw new Error('Posiadasz już formularz w podanym zakresie dat!');

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
            buttons={[
                {text: 'Chat', link: '/chat'},
                {text: 'Wydarzenia', link: '/events'},
                {text: 'Pokoje wspólne', link: '/common-rooms'},
                {text: 'Pokój', link: '/rooms/myInfo'},
                {text: 'Zgłoś problem', link: '/problems'}
            ]}
            footerContent={<p></p>}
        >
            <div className="flex flex-col md:flex-row w-full">
                {/* Lewa kolumna */}
                <div className="w-full md:w-1/4 flex justify-center items-start p-5">
                    <button
                        type="button"
                        className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                        onClick={() => window.history.back()}
                    >
                        ← Powrót
                    </button>
                </div>

                {/* Środkowa kolumna */}
                <div className="w-full md:w-2/4 flex justify-center items-start p-5">
                    <div className="w-full max-w-lg bg-gray-100 p-5 rounded-lg shadow-md">
                        <h2 className="text-2xl font-semibold text-gray-600 text-center mb-4">
                            Złóż wniosek o akademik
                        </h2>

                        {error && <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4">{error}</div>}
                        {successMessage && <div className="bg-green-100 text-gray-600 p-3 rounded-lg mb-4">{successMessage}</div>}

                        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                            <div>
                                <label htmlFor="startDate" className="block text-gray-700 mb-1">Data rozpoczęcia</label>
                                <input
                                    type="date"
                                    id="startDate"
                                    value={startDate}
                                    min={today}
                                    onChange={(e) => {
                                        const value = e.target.value;
                                        setStartDate(value);
                                        if (endDate && endDate < value) {
                                            setEndDate('');
                                        }
                                    }}
                                    required
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>

                            <div>
                                <label htmlFor="endDate" className="block text-gray-700 mb-1">Data zakończenia (opcjonalna)</label>
                                <input
                                    type="date"
                                    id="endDate"
                                    value={endDate}
                                    min={startDate || today}
                                    onChange={(e) => setEndDate(e.target.value)}
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>

                            <div>
                                <label htmlFor="income" className="block text-gray-700 mb-1">
                                    Dochód miesięczny (netto) na osobę w gospodarstwie domowym (PLN)
                                </label>
                                <input
                                    type="number"
                                    id="income"
                                    min={0}
                                    value={income}
                                    onChange={(e) => handleIncomeChange(e.target.value)}
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>

                            <div>
                                <label htmlFor="locationSearch" className="block text-gray-700 mb-1">
                                    Wpisz lokalizację (miasto, adres, kod pocztowy):
                                </label>
                                <div className="flex gap-2">
                                    <input
                                        type="text"
                                        id="locationSearch"
                                        value={searchQuery}
                                        onChange={(e) => setSearchQuery(e.target.value)}
                                        className="min-w-0 flex-1 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                        placeholder="np. Piotrkowska 123, Łódź"
                                    />
                                    <button
                                        type="button"
                                        className="whitespace-nowrap bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition"
                                        onClick={async () => {
                                            const result = await geocodeLocation(searchQuery);
                                            if (result) {
                                                handleLocationSelect(result);
                                            } else {
                                                setError('Nie znaleziono lokalizacji');
                                            }
                                        }}
                                    >
                                        Szukaj
                                    </button>
                                </div>
                            </div>

                            <div>
                                <label className="block text-gray-700 mb-1">Zaznacz dokładnie swoje miejsce zamieszkania:</label>
                                <MapContainer
                                    center={[51.74899574307592, 19.45339553079945]}
                                    zoom={10}
                                    scrollWheelZoom
                                    style={{ height: '300px', width: '100%' }}
                                >
                                    <TileLayer
                                        attribution='&copy; <a href="https://carto.com/">CARTO</a>'
                                        url="https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png"
                                    />
                                    {selectedLatLng && <Marker position={selectedLatLng} />}
                                    {selectedLatLng && <MapCenterer latlng={selectedLatLng} />}
                                    <LocationPicker onLocationSelect={handleLocationSelect} />
                                </MapContainer>
                            </div>

                            <div>
                                <label htmlFor="comments" className="block text-gray-700 mb-1">Komentarze</label>
                                <textarea
                                    id="comments"
                                    value={comments}
                                    onChange={(e) => setComments(e.target.value)}
                                    className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-300"
                                />
                            </div>

                            <button
                                type="submit"
                                className="w-full bg-gray-700 text-white py-2 rounded-lg hover:bg-gray-600 transition"
                            >
                                Złóż formularz
                            </button>
                        </form>
                    </div>
                </div>

                {/* Prawa kolumna */}
                <div className="w-full md:w-1/4"></div>
            </div>

        </Template>
    );
}

export default DormFormPage;
