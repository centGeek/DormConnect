import React, {useState} from "react";
import Template from "../../Template/Template.tsx";

type ResidentReassignmentPreview = {
    residentId: number;
    residentName: string; // lub "N/A"
    fromDate: string;
    toDate: string;
    plannedNewStartDate: string | null;
    roomAvailable: boolean;
};

type DeleteRoomImpactPreviewDTO = {
    roomId: number;
    canDeleteNow: boolean;
    minRelocationDate: string | null;
    currentResidents: ResidentReassignmentPreview[];
    futureResidents: ResidentReassignmentPreview[];
};

interface RoomDeletionProps {
    roomId: number;
}

export const RoomDeletion: React.FC<RoomDeletionProps> = ({roomId}) => {
    const [simulation, setSimulation] = useState<DeleteRoomImpactPreviewDTO | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [confirmed, setConfirmed] = useState(false);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [modalOpen, setModalOpen] = useState(false);

    async function fetchSimulation() {
        setLoading(true);
        setError(null);
        setSuccessMessage(null);

        const token = document.cookie
            .split("; ")
            .find(row => row.startsWith("token="))
            ?.split("=")[1];

        if (!token) {
            setError("Brak tokena autoryzacyjnego");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch(`/api/dorm/rooms/${roomId}?areYouSure=false`, {
                method: "DELETE",
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include',
            });

            if (response.status === 409) {
                const data: DeleteRoomImpactPreviewDTO = await response.json();
                setSimulation(data);
                setModalOpen(true); // otwieramy modal przy symulacji
            } else if (response.ok) {
                setSuccessMessage("Pokój usunięty pomyślnie.");
            } else {
                const text = await response.text();
                setError(`Błąd: ${text}`);
            }
        } catch (e) {
            setError("Błąd sieciowy");
        }
        setLoading(false);
    }

    async function confirmDeletion() {
        setLoading(true);
        setError(null);
        setSuccessMessage(null);

        const token = document.cookie
            .split('; ')
            .find(row => row.startsWith("token="))
            ?.split('=')[1];

        if (!token) {
            setError("Brak tokena autoryzacyjnego");
            setLoading(false);
            return;
        }

        try {
            const response = await fetch(`/api/dorm/rooms/${roomId}?areYouSure=true`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                credentials: "include",
            });

            if (response.ok) {
                setSuccessMessage("Pokój usunięty, mieszkańcy przeniesieni.");
                setSimulation(null);
                setConfirmed(true);
                setModalOpen(false);
            } else {
                const text = await response.text();
                setError(`Błąd podczas usuwania: ${text}`);
            }
        } catch {
            setError("Błąd sieciowy");
        }
        setLoading(false);
    }

    return (
        <div className="p-4">
            <button
                onClick={fetchSimulation}
                disabled={loading || confirmed}
                className="bg-red-600 hover:bg-red-700 disabled:opacity-50 text-white font-semibold py-2 px-4 rounded"
            >
                Usuń
            </button>

            {loading && <p className="mt-2 text-gray-700">Ładowanie...</p>}

            {error && <p className="mt-2 text-red-600 font-semibold">{error}</p>}

            {successMessage && <p className="mt-2 text-green-600 font-semibold">{successMessage}</p>}

            {/* MODAL */}
            {modalOpen && simulation && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
                    onClick={() => setModalOpen(false)} // zamknięcie modalu po kliknięciu poza box
                >
                    <div
                        className="bg-white rounded-lg shadow-lg max-w-3xl w-full p-6 relative"
                        onClick={(e) => e.stopPropagation()} // zapobiega zamknięciu po kliknięciu w modal content
                    >
                        <button
                            className="absolute top-3 right-3 text-gray-500 hover:text-gray-800 font-bold text-xl"
                            onClick={() => setModalOpen(false)}
                            aria-label="Zamknij modal"
                        >
                            &times;
                        </button>

                        <h2 className="text-xl font-bold mb-4">Symulacja usunięcia pokoju {simulation.roomId}</h2>

                        <p>
                            <span className="font-semibold">Można usunąć od razu?</span>{" "}
                            <span className={simulation.canDeleteNow ? "text-green-600" : "text-red-600"}>
                  {simulation.canDeleteNow ? "Tak" : "Nie - wymagane przeniesienia"}
                </span>
                        </p>

                        <p className="mb-4">
                            <span className="font-semibold">Najwcześniejsza data przeniesienia:</span>{" "}
                            {simulation.minRelocationDate ?? "Brak"}
                        </p>

                        <div className="mb-4">
                            <h3 className="font-semibold mb-2">Mieszkańcy obecni:</h3>
                            {simulation.currentResidents.length > 0 ? (
                                <table className="w-full text-left border-collapse border border-gray-300">
                                    <thead>
                                    <tr className="bg-gray-100">
                                        <th className="border border-gray-300 px-2 py-1">ID</th>
                                        <th className="border border-gray-300 px-2 py-1">Imię</th>
                                        <th className="border border-gray-300 px-2 py-1">Okres</th>
                                        <th className="border border-gray-300 px-2 py-1">Status przeniesienia</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {simulation.currentResidents.map((r) => (
                                        <tr key={r.residentId} className="odd:bg-white even:bg-gray-50">
                                            <td className="border border-gray-300 px-2 py-1">{r.residentId}</td>
                                            <td className="border border-gray-300 px-2 py-1">{r.residentName || "N/A"}</td>
                                            <td className="border border-gray-300 px-2 py-1">
                                                {r.fromDate} - {r.toDate}
                                            </td>
                                            <td
                                                className={`border border-gray-300 px-2 py-1 ${
                                                    r.roomAvailable ? "text-green-600" : "text-red-600"
                                                }`}
                                            >
                                                {r.roomAvailable
                                                    ? `Przeniesienie od: ${r.plannedNewStartDate}`
                                                    : "Brak dostępnego pokoju"}
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            ) : (
                                <p>Brak obecnych mieszkańców</p>
                            )}
                        </div>

                        <div className="mb-6">
                            <h3 className="font-semibold mb-2">Mieszkańcy przyszli:</h3>
                            {simulation.futureResidents.length > 0 ? (
                                <table className="w-full text-left border-collapse border border-gray-300">
                                    <thead>
                                    <tr className="bg-gray-100">
                                        <th className="border border-gray-300 px-2 py-1">ID</th>
                                        <th className="border border-gray-300 px-2 py-1">Imię</th>
                                        <th className="border border-gray-300 px-2 py-1">Okres</th>
                                        <th className="border border-gray-300 px-2 py-1">Status przeniesienia</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {simulation.futureResidents.map((r) => (
                                        <tr key={r.residentId} className="odd:bg-white even:bg-gray-50">
                                            <td className="border border-gray-300 px-2 py-1">{r.residentId}</td>
                                            <td className="border border-gray-300 px-2 py-1">{r.residentName || "N/A"}</td>
                                            <td className="border border-gray-300 px-2 py-1">
                                                {r.fromDate} - {r.toDate}
                                            </td>
                                            <td
                                                className={`border border-gray-300 px-2 py-1 ${
                                                    r.roomAvailable ? "text-green-600" : "text-red-600"
                                                }`}
                                            >
                                                {r.roomAvailable
                                                    ? `Przeniesienie od: ${r.plannedNewStartDate}`
                                                    : "Brak dostępnego pokoju"}
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            ) : (
                                <p>Brak przyszłych mieszkańców</p>
                            )}
                        </div>

                        <div className="flex justify-end space-x-3">
                            <button
                                onClick={() => setModalOpen(false)}
                                disabled={loading}
                                className="px-4 py-2 rounded border border-gray-400 hover:bg-gray-100"
                            >
                                Anuluj
                            </button>
                            <button
                                onClick={confirmDeletion}
                                disabled={loading}
                                className="bg-red-600 hover:bg-red-700 text-white font-semibold px-4 py-2 rounded disabled:opacity-50"
                            >
                                Potwierdź usunięcie pokoju i przeniesienie mieszkańców
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default RoomDeletion;
