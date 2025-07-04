import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Template from "../../Template/Template.tsx";
import {useNavigate} from "react-router-dom";
import {buttons} from "../../ReusableComponents/buttons.ts";

interface DormFormDTO {
    id: number;
    startDate: string;
    endDate: string;
    processed: boolean;
    comments: string;
    priorityScore: number;
}

interface AssignmentsDTO {
    id: number;
    userId: number;
    userFullName: string;
    roomNumber: string;
    roomFloor: number;
    startDate: string;
    endDate: string | null;
}

const MyDormRoomsInfo: React.FC = () => {
    const navigate = useNavigate();
    const [forms, setForms] = useState<DormFormDTO[]>([]);
    const [assignments, setAssignments] = useState<AssignmentsDTO[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [view, setView] = useState<'current' | 'historical'>('current');
    const [endingAssignmentId, setEndingAssignmentId] = useState<number | null>(null);
    const [newEndDate, setNewEndDate] = useState<string>('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = document.cookie
                    .split("; ")
                    .find(row => row.startsWith("token="))
                    ?.split("=")[1];

                if (!token) {
                    setError("User not authenticated");
                    navigate("/login");
                    return;
                }

                const [formResponse, assignResponse] = await Promise.all([
                    axios.get<DormFormDTO[]>("/api/dorm/form/me", {
                        headers: {Authorization: `Bearer ${token}`}
                    }),
                    axios.get<AssignmentsDTO[]>("/api/dorm/assign/myAssigns", {
                        headers: {Authorization: `Bearer ${token}`}
                    })
                ]);

                setForms(formResponse.data);
                setAssignments(assignResponse.data);
            } catch (err) {
                console.error("Error fetching dorm data:", err);
                setError("Failed to load dorm data.");
            }
        };

        fetchData();
    }, []);

    const now = new Date();
    const nowIso = now.toISOString();

    const filteredAssignments = assignments
        .filter(a => view === 'current' ? !a.endDate || a.endDate >= nowIso : a.endDate && a.endDate < nowIso)
        .sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime());

    const isOngoing = (a: AssignmentsDTO) => {
        const start = new Date(a.startDate);
        const end = a.endDate ? new Date(a.endDate) : null;
        return start <= now && (!end || end >= now);
    };

    const submitEndEarlier = async (id: number) => {
        try {
            const token = document.cookie
                .split("; ")
                .find(row => row.startsWith("token="))
                ?.split("=")[1];

            await axios.put(`/api/dorm/assign/${id}/shorten?newEndDate=${newEndDate}`, null, {
                headers: {Authorization: `Bearer ${token}`}
            });

            window.location.reload();
        } catch (e) {
            console.error("Failed to end assignment earlier", e);
            setError("Failed to submit earlier end date.");
        }
    };

    const [confirmingId, setConfirmingId] = useState<number | null>(null);


    const handleClick = (id: number) => {
        if (confirmingId === id) {
            handleDeleteForm(id);
        } else {
            setConfirmingId(id);
            setTimeout(() => {
                setConfirmingId(null);
            }, 2000);
        }
    };
    const handleDeleteForm = async (formId: number) => {
        try {
            const token = document.cookie
                .split("; ")
                .find(row => row.startsWith("token="))
                ?.split("=")[1];

            if (!token) {
                setError("Brak autoryzacji.");
                return;
            }

            await axios.put(`/api/dorm/form/${formId}/deactivate`, null, {
                headers: {Authorization: `Bearer ${token}`}
            });

            // Usuń formularz z widoku
            setForms(prev => prev.filter(f => f.id !== formId));
        } catch (e) {
            console.error("Nie udało się usunąć formularza", e);
            setError("Nie udało się usunąć formularza.");
        }
    };


    return (
        <Template
            buttons={buttons}
            footerContent={<p></p>}
        >
            <div className="p-6 max-w-4xl mx-auto space-y-10 text-center">
                <section>
                    <h2 className="text-2xl font-bold mb-4">Twoje formularze</h2>
                    {forms.length === 0 ? (
                        <p>Brak zgłoszonych formularzy.</p>
                    ) : (
                        <ul className="space-y-4">
                            {forms.map(form => (
                                <li key={form.id} className="border rounded p-4 shadow">
                                    <p><strong>Od:</strong> {form.startDate}</p>
                                    <p><strong>Do:</strong> {form.endDate}</p>
                                    <p>
                                        <strong>Status:</strong> {form.processed ? "Rozpatrzono" : "Oczekujący na rozpatrzenie"}
                                    </p>
                                    {form.comments && <p><strong>Komentarz:</strong> {form.comments}</p>}
                                    <p><strong>Wynik:</strong> {form.priorityScore}</p>

                                    {!form.processed && (
                                        <button
                                            key={form.id}
                                            onClick={() => handleClick(form.id)}
                                            className={`px-4 py-2 rounded text-white transition-colors duration-300
            ${confirmingId === form.id ? 'bg-red-600 hover:bg-red-700' : 'bg-gray-400 hover:bg-gray-500'}`}
                                        >
                                            {confirmingId === form.id ? "Na pewno?" : "Usuń"}
                                        </button>
                                    )}
                                </li>
                            ))}

                        </ul>
                    )}
                </section>
                <button
                    className="bg-gray-500 text-white border  px-4 py-2 rounded-lg shadow hover:bg-white hover:text-gray-500 transition"
                    onClick={() => navigate("/rooms/form")}
                >
                    Zgłoś wniosek
                </button>
                <section>
                    <h2 className="text-2xl font-bold mb-4">Twoje przydziały</h2>
                    <div className="mb-4 flex items-center justify-center gap-4">
                        <span
                            className={`font-medium ${view === 'current' ? 'text-gray-600' : 'text-gray-300'}`}>Aktualne</span>
                        <button
                            onClick={() => setView(view === 'current' ? 'historical' : 'current')}
                            className={`w-14 h-8 flex items-center bg-gray-400 rounded-full p-1 transition-colors duration-300 ${
                                view === 'historical' ? 'bg-gray-500' : ''
                            }`}
                        >
                            <div
                                className={`bg-white w-6 h-6 rounded-full shadow-md transform transition-transform duration-300 ${
                                    view === 'historical' ? 'translate-x-6' : ''
                                }`}
                            ></div>
                        </button>
                        <span
                            className={`font-medium ${view === 'historical' ? 'text-gray-600' : 'text-gray-300'}`}>Historyczne</span>
                    </div>
                    <ul className="space-y-4">
                        {filteredAssignments.map(a => (
                            <li
                                key={a.id}
                                className="border rounded p-4 shadow bg-white hover:bg-gray-100 hover:shadow-md hover:-translate-y-1 transition-all duration-200"
                            >
                                <p><strong>Pokój:</strong> {a.roomNumber} (Piętro {a.roomFloor})</p>
                                <p><strong>Okres:</strong> {a.startDate} – {a.endDate ?? 'trwa'}</p>
                                {isOngoing(a) && (
                                    <div className="mt-2">
                                        {endingAssignmentId === a.id ? (
                                            <div className="flex flex-col justify-center sm:flex-row gap-3">
                                                <input
                                                    type="date"
                                                    value={newEndDate}
                                                    min={new Date().toISOString().split('T')[0]}
                                                    max={new Date(a.endDate).toISOString().split('T')[0]}
                                                    onChange={e => setNewEndDate(e.target.value)}
                                                    className="border rounded px-3 py-1"
                                                />
                                                <button
                                                    onClick={() => submitEndEarlier(a.id)}
                                                    className="bg-gray-600 text-white px-3 py-1 rounded"
                                                >
                                                    Zatwierdź
                                                </button>
                                                <button
                                                    onClick={() => setEndingAssignmentId(null)}
                                                    className="text-sm text-gray-500 hover:text-gray-700"
                                                >
                                                    Anuluj
                                                </button>
                                            </div>
                                        ) : (
                                            <button
                                                onClick={() => {
                                                    setEndingAssignmentId(a.id);
                                                    setNewEndDate('');
                                                }}
                                                className="bg-gray-600 text-white px-4 py-1 rounded"
                                            >
                                                Zakończ wcześniej
                                            </button>
                                        )}
                                    </div>
                                )}
                            </li>
                        ))}
                    </ul>
                </section>
            </div>
        </Template>
    );
};

export default MyDormRoomsInfo;
