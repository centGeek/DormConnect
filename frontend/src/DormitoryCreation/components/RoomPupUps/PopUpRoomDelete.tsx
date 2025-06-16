import React, {useState} from "react";
import Cookies from "js-cookie";

interface ResidentReassignmentPreview {
    userId: number;
    fullName: string;
    fromDate: string;
    toDate: string;
    plannedNewStartDate: string | null;
    roomAvailable: boolean;
}

interface DeleteRoomImpactPreviewDTO {
    roomId: number;
    canDeleteNow: boolean;
    minRelocationDate: string | null;
    currentResidents: ResidentReassignmentPreview[];
    futureResidents: ResidentReassignmentPreview[];
}

interface PopUpRoomDeleteProps {
    onClose: () => void;
    roomNumber: string;
    roomId: number;
}

function PopUpRoomDelete({onClose, roomNumber, roomId}: PopUpRoomDeleteProps) {
    const [loading, setLoading] = useState(false);
    const [simulation, setSimulation] = useState<DeleteRoomImpactPreviewDTO | null>(null);
    const [error, setError] = useState<string | null>(null);

    const fetchDeleteSimulation = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`/api/dorm/rooms/${roomId}?areYouSure=false`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${Cookies.get("token")}`,
                },
                credentials: "include",
            });

            if (response.status === 266) {
                const data: DeleteRoomImpactPreviewDTO = await response.json();
                setSimulation(data);
            } else if (response.ok) {
                onClose(); // usunięcie bez przeszkód
            } else {
                const text = await response.text();
                setError(text);
            }
        } catch {
            setError("Błąd sieci");
        }
        setLoading(false);
    };

    const confirmDeletion = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`/api/dorm/rooms/${roomId}?areYouSure=true`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${Cookies.get("token")}`,
                },
                credentials: "include",
            });

            if (response.ok) {
                onClose();
            } else {
                const text = await response.text();
                setError(text);
            }
        } catch {
            setError("Błąd sieci");
        }
        setLoading(false);
    };

    const renderTable = (title: string, residents: ResidentReassignmentPreview[]) => (
        <div className="mb-4">
            <h2 className="font-semibold mb-2">{title}</h2>
            <table className="w-full border text-sm">
                <thead className="bg-gray-100">
                <tr>
                    <th className="border px-2 py-1">ID</th>
                    <th className="border px-2 py-1">Imię i nazwisko</th>
                    <th className="border px-2 py-1">Od</th>
                    <th className="border px-2 py-1">Do</th>
                    <th className="border px-2 py-1">Nowy start</th>
                    <th className="border px-2 py-1">Pokój dostępny</th>
                </tr>
                </thead>
                <tbody>
                {residents.map((r, index) => (
                    <tr key={`${r.userId}-${index}`}>
                        <td className="border px-2 py-1 text-center">{r.userId}</td>
                        <td className="border px-2 py-1">{r.fullName}</td>
                        <td className="border px-2 py-1 text-center">{r.fromDate}</td>
                        <td className="border px-2 py-1 text-center">{r.toDate}</td>
                        <td className="border px-2 py-1 text-center">{r.plannedNewStartDate ?? "–"}</td>
                        <td className={`border px-2 py-1 text-center ${r.roomAvailable ? "text-green-600" : "text-red-600"}`}>
                            {r.roomAvailable ? "Tak" : "Nie"}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );

    return (
        <div className="fixed inset-0 border border-gray-500 flex items-center justify-center z-50 ">
            <div
                className="bg-white p-6 border border-gray-500 rounded-lg shadow-lg  max-h-[90vh] overflow-auto relative">
                <h1 className="text-xl font-bold mb-4 text-gray-700">Usuń pokój</h1>

                {!simulation ? (
                    <>
                        <p className="text-gray-600 mb-6">
                            Czy na pewno chcesz usunąć pokój <strong>{roomNumber}</strong>? Tej operacji nie można
                            cofnąć.
                        </p>
                        <div className="flex justify-between">
                            <button
                                onClick={fetchDeleteSimulation}
                                disabled={loading}
                                className="bg-red-500 text-white border border-red-500 px-4 py-2 rounded hover:bg-white hover:text-red-600 transition"
                            >
                                Usuń
                            </button>
                            <button
                                onClick={onClose}
                                className="bg-white text-gray-600 border border-gray-500 px-4 py-2 rounded hover:bg-gray-500 hover:text-white transition"
                            >
                                Anuluj
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <p className="text-gray-600 mb-2">
                            <strong>Najwcześniejsza możliwa data przeniesienia mieszkańców:</strong>{" "}
                            {simulation.minRelocationDate ?? "Brak"}
                        </p>
                        <p className="text-gray-600 mb-4">
                            <strong>Można usunąć teraz?</strong>{" "}
                            <span className={simulation.canDeleteNow ? "text-green-600" : "text-red-600"}>
                {simulation.canDeleteNow ? "Tak" : "Nie"}
              </span>
                        </p>

                        {simulation.currentResidents.length > 0 &&
                            renderTable("Obecni mieszkańcy", simulation.currentResidents)}
                        {simulation.futureResidents.length > 0 &&
                            renderTable("Przyszli mieszkańcy", simulation.futureResidents)}

                        <div className="flex justify-between mt-4">
                            <button
                                onClick={confirmDeletion}
                                disabled={loading || !simulation.canDeleteNow}
                                className={`px-4 py-2 rounded border transition ${
                                    simulation.canDeleteNow
                                        ? "bg-red-500 text-white border-red-500 hover:bg-white hover:text-red-600"
                                        : "bg-gray-200 text-gray-400 border-gray-300 cursor-not-allowed"
                                }`}
                            >
                                Potwierdź usunięcie
                            </button>

                            <button
                                onClick={() => setSimulation(null)}
                                className="bg-white text-gray-600 border border-gray-500 px-4 py-2 rounded hover:bg-gray-500 hover:text-white transition"
                            >
                                Wróć
                            </button>
                        </div>
                    </>
                )}

                {error && <p className="text-red-600 mt-4">{error}</p>}
            </div>
        </div>
    );
}

export default PopUpRoomDelete;
