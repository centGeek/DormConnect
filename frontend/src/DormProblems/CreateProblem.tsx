import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import Template from '../Template/Template';
import { UserContext } from '../Context/UserContext';

function DormProblemCreate() {
    const navigate = useNavigate();
    const [problemDesc, setProblemDesc] = useState('');
    const [problemDate, setProblemDate] = useState('');
    const [problemName, setProblemName] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const userContext = useContext(UserContext);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');
        setSuccessMessage('');

        const createdDormProblem = {
            name: problemName,
            description: problemDesc,
            problemDate: problemDate,
        };

        try {
            const response = await fetch('/api/dorm-problem/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userContext?.token}`
                },
                body: JSON.stringify(createdDormProblem),
                credentials: 'include'
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Wystąpił błąd podczas dodawania problemu');
            }

            setSuccessMessage('Zgłoszenie zostało dodane pomyślnie!');
            setTimeout(() => navigate('/problems'), 100);

        } catch (err: any) {
            console.error(err);
            setError(err.message);
        }
    };

    useEffect(() => {}, []);

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
            <div className="flex flex-col md:flex-row w-full">
                {/* Lewa kolumna */}
                <div className="w-full md:w-1/4 flex justify-center items-start p-5">
                    <button
                        type="button"
                        className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                        onClick={() => navigate(-1)}
                    >
                        ← Powrót
                    </button>
                </div>

                {/* Środkowa kolumna */}
                <div className="w-full md:w-2/4 flex justify-center items-start p-5">
                    <div className="w-full max-w-lg bg-gray-100 p-5 rounded-lg shadow-md">
                        <h2 className="text-2xl font-semibold text-gray-600 text-center mb-4">Zgłoś Problem</h2>

                        {error && (
                            <div className="bg-red-100 text-red-600 p-3 rounded-lg mb-4 whitespace-pre-line">
                                {error}
                            </div>
                        )}
                        {successMessage && (
                            <div className="bg-green-100 text-gray-700 p-3 rounded-lg mb-4">
                                {successMessage}
                            </div>
                        )}

                        <form className="space-y-6" onSubmit={handleSubmit}>
                            <div className='space-y-2'>
                                <label className="block text-gray-700 font-medium">Nazwa problemu:</label>
                                <input
                                    type='text'
                                    name='name'
                                    value={problemName}
                                    onChange={(e) => setProblemName(e.target.value)}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500"
                                    required
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="block text-gray-700 font-medium">Data wystąpienia problemu:</label>
                                <input
                                    type="date"
                                    name="problemDate"
                                    value={problemDate}
                                    max={new Date().toISOString().split('T')[0]}
                                    onChange={(e) => setProblemDate(e.target.value)}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500"
                                    required
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="block text-gray-700 font-medium">Opis:</label>
                                <textarea
                                    name="problemDesc"
                                    value={problemDesc}
                                    placeholder="Opisz zdarzenie"
                                    onChange={(e) => setProblemDesc(e.target.value)}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500 min-h-[120px]"
                                    required
                                />
                            </div>

                            <button
                                type="submit"
                                className="w-full bg-gray-800 text-white font-bold py-3 px-4 rounded-lg hover:bg-gray-700 transition duration-300"
                            >
                                Zgłoś problem
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

export default DormProblemCreate;
