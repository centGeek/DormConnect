import { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import Template from '../Template/Template';
import TokenJwtPayload from './TokenJwtPayload';
import { UserContext } from '../Context/UserContext';
import translateStatus from "./components/TranslateProblemStatus.tsx";


interface DormProblem {
    id: number;
    studentId: number;
    name: string;
    description: string;
    problemDate: string;
    submittedDate: string;
    problemStatus: string

}

function DormProblems() {
    const navigate = useNavigate();
    let decodedToken: TokenJwtPayload;
    const [isAdmin, setIsAdmin] = useState<boolean>(false);
    const userContext = useContext(UserContext);

    const [dormProblems, setDormProblems] = useState<DormProblem[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [filters, setFilters] = useState({
        searchTerm: '',
        statusFilter: 'ALL'
    });

    const handleButtonClick = () => navigate('/problems/create');

    const filteredProblems = dormProblems.filter(problem => {
        const matchesSearch = problem.name.toLowerCase()
            .includes(filters.searchTerm.toLowerCase());
        const matchesStatus = filters.statusFilter === 'ALL' ||
            problem.problemStatus === filters.statusFilter;
        return matchesSearch && matchesStatus;
    });


    const handleViewDetails = (problemId: number) => {
        navigate('/problems/details/' + problemId);
    };

    const handleManageButtonClick = (problemId: number) => {
        navigate('/problems/manage/' + problemId);
    };

    const fetchStudentData = async () => {
        try {
            setLoading(true);
            console.log(isAdmin);
            const response = await fetch(`/api/dorm-problem/get`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${userContext?.token}`
                },
                credentials: 'include'
            });
            const data = await response.json();
            setDormProblems(data.content || data || []);
        } catch (err) {
            console.error('Error fetching dormitory problems');
        } finally {
            setLoading(false);
        }
    };

    const fetchDormProblems = async () => {
        try {
            setLoading(true);


            console.log(userContext?.token);
            if (userContext?.user?.roles.includes('ADMIN')) {
                setIsAdmin(true);
            } else {
                setIsAdmin(false);
            }
            console.log(isAdmin);
            const response = await fetch(`/api/dorm-problem/get`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${userContext?.token}`
                },
                credentials: 'include'
            });
            const data = await response.json();
            setDormProblems(data.content || data || []);
        } catch (err) {
            console.error('Error fetching dormitory problems');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDormProblems();
    }, []);

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms'},
            {text: 'Zgłoś problem', link: '/problems'},
        ]}>
            <div className="p-6">
                <div className="text-center mb-8">
                    <h2 className="text-4xl font-bold text-gray-800">Zgłoszenia</h2>
                </div>
                
                <div className="flex flex-col md:flex-row gap-4 items-center justify-between mb-8">
                    <div className="w-full md:w-1/3">
                        <input
                            type="text"
                            placeholder="Znajdź zdarzenie po opisie"
                            value={filters.searchTerm}
                            onChange={(e) => setFilters({ ...filters, searchTerm: e.target.value })}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500 shadow-sm"
                        />
                    </div>
                    
                    <div className="w-full md:w-1/4">
                        <select
                            value={filters.statusFilter}
                            onChange={(e) => setFilters({ ...filters, statusFilter: e.target.value })}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500 shadow-sm"
                        >
                            <option value="ALL">Wszystkie</option>
                            <option value="IN_PROGRESS">W trakcie</option>
                            <option value="SUBMITTED">Przyjęte</option>
                            <option value="RESOLVED">Rozwiązane</option>
                            <option value="REJECTED">Odrzucone</option>
                        </select>
                    </div>
                    
                    {!isAdmin && (
                        <button
                            className="w-full md:w-auto bg-gray-600 text-white font-bold py-2 px-6 rounded-lg hover:bg-white hover:text-gray-800 border border-gray-800 transition duration-300 shadow-sm"
                            onClick={handleButtonClick}
                        >
                            Zgłoś problem
                        </button>
                    )}
                </div>
                
                {loading ? (
                    <div className="text-center text-gray-500 py-8">Ładowanie zdarzeń...</div>
                ) : filteredProblems.length === 0 ? (
                    <div className="text-center text-gray-500 py-8">Nie znaleziono zdarzeń, które spełniają podane kryteria</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {filteredProblems.map((problem) => (
                            <div
                                key={problem.id}
                                className="p-6 bg-white border border-gray-200 rounded-lg shadow-md hover:shadow-lg transition duration-300"
                            >
                                <h3 className="text-xl font-bold text-gray-800 mb-2">{problem.name}</h3>
                                <p className="text-gray-600 mb-1 font-semibold">Id Studenta: {problem.studentId}</p>
                                <div className="flex items-center mb-2">
                                    <span className="font-semibold mr-2">Status:</span>
                                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                                        problem.problemStatus === "SUBMITTED"
                                            ? "bg-yellow-100 text-yellow-800"
                                            : problem.problemStatus === "RESOLVED"
                                                ? "bg-green-100 text-green-800"
                                                : "bg-red-100 text-red-800"
                                    }`}>
                                        {translateStatus(problem.problemStatus)}
                                    </span>
                                </div>
                                <p className="text-gray-600 mb-1 font-semibold">
                                    Data wystąpenia zdarzenia: {new Date(problem.problemDate).toLocaleDateString()}
                                </p>
                                <p className="text-gray-600 mb-4 font-semibold">
                                    Data przyjęcia zdarzenia: {new Date(problem.submittedDate).toLocaleDateString()}
                                </p>
                                
                                <div className="flex flex-col space-y-2">
                                    {isAdmin && problem.problemStatus != 'RESOLVED' && problem.problemStatus != 'REJECTED' && (
                                        <button
                                            className="w-full bg-red-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-red-700 transition duration-300"
                                            onClick={() => handleManageButtonClick(problem.id)}
                                        >
                                            Zarządzaj
                                        </button>
                                    )}
                                    <button
                                        className="w-full bg-gray-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-gray-700 transition duration-300"
                                        onClick={() => handleViewDetails(problem.id)}
                                    >
                                        Zobacz szczegóły
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </Template>
    );
}

export default DormProblems;
