import { useState, useEffect, useContext } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import translate from './components/TranslateProblemStatus.tsx'
interface DormProblem {
    id: number;
    studentId: number;
    userName: string;
    name: string;
    description: string;
    answer: string | null;
    problemDate: string;
    submittedDate: string;
    problemStatus: string;
}

function DormProblemView() {
    const { problemId } = useParams();
    const navigate = useNavigate();
    const context = useContext(UserContext);

    const [dormProblem, setDormProblem] = useState<DormProblem | null>(null);

    const fetchDormProblem = async () => {
        try {
            const fetchUrl = `/api/dorm-problem/get/${problemId}`;
            const response = await fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${context?.token}`,
                },
                credentials: 'include',
            });

            if (response.ok) {
                const data = await response.json();
                setDormProblem(data);
            } else {
                console.error('Błąd podczas pobierania szczegółów problemu');
                navigate('/problems');
            }
        } catch (err) {
            console.error(err);
            navigate('/problems');
        }
    };

    useEffect(() => {
        fetchDormProblem();
    }, []);

    if (!dormProblem) {
        return (
            <Template buttons={[]}>
                <div className="text-center text-gray-500 py-8">Ładowanie szczegółów problemu...</div>
            </Template>
        );
    }

    return (
        <Template buttons={[
            { text: 'Chat', link: '/chat' },
            { text: 'Wydarzenia', link: '/events' },
            { text: 'Pokoje wspólne', link: '/common-rooms' },
            { text: 'Pokój', link: '/rooms' },
            { text: 'Zgłoś problem', link: '/problems' },
        ]}>
            <div className="max-w-4xl mx-auto p-8 bg-white rounded-lg shadow-lg border border-gray-200">
                <button
                    className="mb-6 px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition duration-300 flex items-center"
                    onClick={() => navigate(-1)}
                >
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                        <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
                    </svg>
                    Powrót
                </button>

                <h3 className="text-3xl font-bold text-gray-800 border-b pb-4 mb-6">Szczegóły problemu</h3>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Nazwa problemu:</h4>
                        <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{dormProblem.name}</p>
                    </div>

                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Opis:</h4>
                        <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{dormProblem.description}</p>
                    </div>

                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Nazwa użytkownika:</h4>
                        <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{dormProblem.userName}</p>
                    </div>



                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Odpowiedź:</h4>
                        <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{dormProblem.answer || 'Brak odpowiedzi'}</p>
                    </div>

                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Data wystąpienia problemu:</h4>
                        <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{new Date(dormProblem.problemDate).toLocaleDateString()}</p>
                    </div>

                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Data zgłoszenia:</h4>
                        <p className="text-gray-800 bg-gray-100 p-3 rounded-md">{new Date(dormProblem.submittedDate).toLocaleDateString()}</p>
                    </div>
                    <div>
                        <h4 className="text-lg font-semibold text-gray-700">Status:</h4>
                        <p className={`text-white p-3 rounded-md ${
                            dormProblem.problemStatus === 'SUBMITTED'
                                ? 'bg-yellow-500'
                                : dormProblem.problemStatus === 'RESOLVED'
                                    ? 'bg-green-500'
                                    : 'bg-red-500'
                        }`}>
                            {translate(dormProblem.problemStatus)}
                        </p>
                    </div>
                </div>

            </div>
        </Template>
    );
}

export default DormProblemView;