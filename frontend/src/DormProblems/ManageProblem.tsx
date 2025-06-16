import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import translate from './components/TranslateProblemStatus.tsx';
import {buttons} from "../ReusableComponents/buttons.ts";

interface DormProblem {
    id: number;
    studentId: number;
    userName: string;
    name: string;
    description: string;
    answer: string| null;
    problemDate: string;
    submittedDate: string;
    problemStatus: string;
}

function DormProblemManage() {
    const { problemId } = useParams();
    const navigate = useNavigate();
    const userContext = useContext(UserContext);

    const [problemAnswer, setProblemAnswer] = useState('');
    const [problemStatus, setProblemStatus] = useState('');
    const [dormProblemStatuses, setDormProblemStatuses] = useState<string[]>([]);
    const [dormProblem, setDormProblem] = useState<DormProblem | null>(null);

    const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            const updatedProblem = {
                id: dormProblem?.id,
                studentId: dormProblem?.studentId,
                description: dormProblem?.description,
                problemStatus,
                answer: problemAnswer,
            };

            await fetch(`/api/dorm-problem/update/${dormProblem?.id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userContext?.token}`,
                },
                body: JSON.stringify(updatedProblem),
                credentials: 'include',
            });

            navigate('/problems');
        } catch (err) {
            console.error(err);
        }
    };

    const fetchProblemStatuses = async () => {
        try {
            const response = await fetch('/api/dorm-problem/problem-statuses', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userContext?.token}`,
                },
                credentials: 'include',
            });

            const data = await response.json();
            setDormProblemStatuses(data || []);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDormProblem = async () => {
        try {
            const response = await fetch(`/api/dorm-problem/get/${problemId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userContext?.token}`,
                },
                credentials: 'include',
            });

            const data = await response.json();
            setDormProblem(data);
            setProblemStatus(data.problemStatus);
            setProblemAnswer(data.answer || '');
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchDormProblem();
        fetchProblemStatuses();
    }, []);

    return (
        <Template
            buttons={buttons}
        >
            <div className="max-w-4xl mx-auto p-8 bg-white rounded-lg shadow-lg border border-gray-200">
                <button
                    onClick={() => navigate('/problems')}
                    className="flex items-center gap-2 mb-6 px-4 py-2 text-white bg-gray-500 rounded-lg hover:bg-gray-600 transition duration-300"
                >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-5 w-5"
                        viewBox="0 0 20 20"
                        fill="currentColor"
                    >
                        <path
                            fillRule="evenodd"
                            d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z"
                            clipRule="evenodd"
                        />
                    </svg>
                    Powrót
                </button>

                {/* Sekcja szczegółów problemu */}
                <div className="mb-8">
                    <h3 className="text-3xl font-bold text-gray-800 border-b pb-4 mb-6">Szczegóły problemu</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <h4 className="text-lg font-semibold text-gray-700">Nazwa:</h4>
                            <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{dormProblem?.name || 'Brak danych'}</p>
                        </div>
                        <div>
                            <h4 className="text-lg font-semibold text-gray-700">Opis:</h4>
                            <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{dormProblem?.description || 'Brak danych'}</p>
                        </div>
                        <div>
                            <h4 className="text-lg font-semibold text-gray-700">Data wystąpienia:</h4>
                            <p className="text-gray-800 bg-gray-100 p-3 rounded-md">
                                {dormProblem?.problemDate ? new Date(dormProblem.problemDate).toLocaleDateString() : 'Brak danych'}
                            </p>
                        </div>
                        <div>
                            <h4 className="text-lg font-semibold text-gray-700">Data zgłoszenia:</h4>
                            <p className="text-gray-800 bg-gray-100 p-3 rounded-md">
                                {dormProblem?.submittedDate ? new Date(dormProblem.submittedDate).toLocaleDateString() : 'Brak danych'}
                            </p>
                        </div>
                        <div>
                            <h4 className="text-lg font-semibold text-gray-700">Status:</h4>
                            <p className={`text-white p-3 rounded-md ${
                                dormProblem?.problemStatus === 'SUBMITTED'
                                    ? 'bg-yellow-500'
                                    : dormProblem?.problemStatus === 'RESOLVED'
                                        ? 'bg-green-500'
                                        : 'bg-red-500'
                            }`}>
                                {translate(dormProblem?.problemStatus)}
                            </p>
                        </div>
                    </div>
                </div>

                {/* Sekcja formularza */}
                <div>
                    <h3 className="text-3xl font-bold text-gray-800 border-b pb-4 mb-6">Zarządzanie problemem</h3>
                    <form onSubmit={handleFormSubmit} className="space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Status:</label>
                            <select
                                className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500"
                                value={problemStatus}
                                onChange={(e) => setProblemStatus(e.target.value)}
                            >
                                {dormProblemStatuses.map((status) => (
                                    <option key={status} value={status}>
                                        {translate(status)}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Odpowiedź:</label>
                            <textarea
                                className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 bg-gray-50"
                                value={problemAnswer}
                                onChange={(e) => setProblemAnswer(e.target.value)}
                            />
                        </div>

                        <button
                            type="submit"
                            className="w-full bg-gray-600 text-white font-bold py-3 px-4 rounded-lg hover:bg-gray-700 transition duration-300"
                        >
                            Zatwierdź zmiany
                        </button>
                    </form>
                </div>
            </div>
        </Template>
    );
}

export default DormProblemManage;