import React, {useState} from "react";
import Template from "../Template/Template.tsx";

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

    async function fetchSimulation() {
        setLoading(true);
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


        try {
            const response = await fetch(`/api/dorm/rooms/${roomId}?areYouSure=false`, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });

            if (response.status === 409) {
                const data: DeleteRoomImpactPreviewDTO = await response.json();
                setSimulation(data);
            } else if (response.ok) {
                // Pokój usunięty od razu (brak przypisań)
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
            .find(row => row.startsWith('token='))
            ?.split('=')[1];

        if (!token) {
            setError('Brak tokena autoryzacyjnego');
            return;
        }


        try {
            const response = await fetch(`/api/dorm/rooms/${roomId}?areYouSure=true`, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });

            if (response.ok) {
                setSuccessMessage("Pokój usunięty, mieszkańcy przeniesieni.");
                setSimulation(null);
                setConfirmed(true);
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
        <Template
            buttons={[{text: 'Chat', link: '/chat'}, {text: 'Akademiki', link: '/dorms'}]}
            footerContent={<p></p>}
        >
            <div>
                <button onClick={fetchSimulation} disabled={loading || confirmed}>
                    Usuń pokój
                </button>

                {loading && <p>Ładowanie...</p>}

                {error && <p style={{color: "red"}}>{error}</p>}

                {successMessage && <p style={{color: "green"}}>{successMessage}</p>}

                {simulation && (
                    <div>
                        <h3>Symulacja usunięcia pokoju {simulation.roomId}</h3>
                        <p>
                            Można usunąć od razu?{" "}
                            {simulation.canDeleteNow ? "Tak" : "Nie - wymagane przeniesienia"}
                        </p>
                        <p>
                            Najwcześniejsza data przeniesienia:{" "}
                            {simulation.minRelocationDate ?? "Brak"}
                        </p>

                        <h4>Mieszkańcy obecni:</h4>
                        <ul>
                            {simulation.currentResidents.map((r) => (
                                <li key={r.residentId}>
                                    ID: {r.residentId}, od: {r.fromDate} do: {r.toDate} →{" "}
                                    {r.roomAvailable
                                        ? `Przeniesienie od: ${r.plannedNewStartDate}`
                                        : "Brak dostępnego pokoju"}
                                </li>
                            ))}
                        </ul>

                        <h4>Mieszkańcy przyszli:</h4>
                        <ul>
                            {simulation.futureResidents.map((r) => (
                                <li key={r.residentId}>
                                    ID: {r.residentId}, od: {r.fromDate} do: {r.toDate} →{" "}
                                    {r.roomAvailable
                                        ? `Przeniesienie od: ${r.plannedNewStartDate}`
                                        : "Brak dostępnego pokoju"}
                                </li>
                            ))}
                        </ul>

                        <button onClick={confirmDeletion} disabled={loading}>
                            Potwierdź usunięcie pokoju i przeniesienie mieszkańców
                        </button>
                    </div>
                )}
            </div>
        </Template>
    );
};
export default RoomDeletion;