import React, { useState } from 'react';
import Template from '../Template/Template';
import { parseJwt } from '../JWT/JWTDecoder.tsx';
import './DormFormPage.css';

function DormFormPage() {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [comments, setComments] = useState('');
    const [priorityScore, setPriorityScore] = useState<number | ''>('');
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);

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

        if (!userId) {
            setError('Nieprawidłowy token użytkownika');
            return;
        }

        const dormForm = {
            startDate,
            endDate: endDate || null,
            comments,
            priorityScore: priorityScore !== '' ? Number(priorityScore) : null,
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

            setSuccessMessage('Formularz został pomyślnie złożony!');
            setStartDate('');
            setEndDate('');
            setComments('');
            setPriorityScore('');
        } catch (err: any) {
            setError(err.message || 'Wystąpił błąd');
        }
    };

    return (
        <Template
        footerContent={<p></p>}
        buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' },{text: 'Rooms', link: '/rooms'},{ text: 'Assigmnetns', link: '/rooms/assignment'},{text: 'Form', link: '/rooms/form'}]}>

            <div className="events-create-container">
                <h2>Złóż wniosek o akademik</h2>

                {error && <p className="error-message">{error}</p>}
                {successMessage && <p className="success-message">{successMessage}</p>}

                <form onSubmit={handleSubmit} className="event-form">
                    <div className="form-group">
                        <label htmlFor="startDate">Data rozpoczęcia</label>
                        <input
                            type="date"
                            id="startDate"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="endDate">Data zakończenia (opcjonalna)</label>
                        <input
                            type="date"
                            id="endDate"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="comments">Komentarze</label>
                        <textarea
                            id="comments"
                            value={comments}
                            onChange={(e) => setComments(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="priorityScore">Wynik priorytetu (1–10)</label>
                        <input
                            type="number"
                            id="priorityScore"
                            min={1}
                            max={10}
                            value={priorityScore}
                            onChange={(e) => setPriorityScore(Number(e.target.value))}
                        />
                    </div>

                    <button type="submit" className="btn btn-primary">Złóż formularz</button>
                </form>
            </div>
        </Template>
    );
}

export default DormFormPage;
