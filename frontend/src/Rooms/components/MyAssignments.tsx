import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Template from "../../Template/Template.tsx";
//TODO start here
const MyAssignments: React.FC = () => {
    // Definicja DTO wewnątrz komponentu
    interface AssignmentsDTO {
        id: number;
        userId: number;
        userFullName: string;
        roomNumber: string;
        roomFloor: number;
        startDate: string; // w formacie ISO, np. "2024-05-01"
        endDate: string;
    }

    const [assignments, setAssignments] = useState<AssignmentsDTO[]>([]); // Ustawiamy jako pustą tablicę
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchAssignments = async () => {
            try {
                // Pobranie tokenu z cookie
                const token = document.cookie
                    .split('; ')
                    .find(row => row.startsWith('token='))?.split('=')[1];

                if (!token) {
                    console.error('Brak tokenu w cookie');
                    setError('Brak tokenu w cookie.');
                    return;
                }

                const user = parseJwt(token); // Zakładamy, że masz funkcję do parsowania JWT
                const userId = user?.id;

                console.log('Pobrany token:', token);
                console.log('ID użytkownika:', userId);

                // Pobranie przydziałów
                const response = await axios.get<AssignmentsDTO[]>('/api/dorm/assign/myAssigns', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                console.log('Dane z endpointa:', response.status); // Logowanie odpowiedzi z API

                setAssignments(response.data); // Zapewniamy, że odpowiedź to tablica
            } catch (err) {
                console.error('Błąd podczas pobierania przydziałów:', err);
                setError('Wystąpił błąd przy ładowaniu przydziałów.');
            }
        };

        fetchAssignments();
    }, []);

    return (
        <Template>
            <div className="p-4">
                <h2 className="text-xl font-bold mb-4">Moje przydziały</h2>
                {error && <p className="text-red-500">{error}</p>}
                {assignments.length === 0 && !error && <p>Brak przydziałów.</p>}
                <ul className="space-y-2">
                    {Array.isArray(assignments) && assignments.map((a) => (
                        <li
                            key={a.id}
                            className="border p-4 rounded-lg shadow-sm bg-white"
                        >
                            <p>
                                <strong>Pokój:</strong> {a.roomNumber} (Piętro {a.roomFloor})
                            </p>
                            <p>
                                <strong>Okres:</strong> {a.startDate} – {a.endDate}
                            </p>
                            <p>
                                <strong>Student:</strong> {a.userFullName}
                            </p>
                        </li>
                    ))}
                </ul>
            </div>
        </Template>
    );
};

// Funkcja do parsowania JWT - możesz ją dostosować, zależnie od tego, jak masz kodowane tokeny
const parseJwt = (token: string) => {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split('')
            .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
            .join('')
    );

    return JSON.parse(jsonPayload);
};

export default MyAssignments;
