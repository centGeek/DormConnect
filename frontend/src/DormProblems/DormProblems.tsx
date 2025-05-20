import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { jwtDecode} from 'jwt-decode';
import Template from '../Template/Template';
import getToken from './GetToken';
import TokenJwtPayload from './TokenJwtPayload';


interface DormProblem {
    id: number;
    studentId: number;
    name: string;
    description: string;
    problemDate: string;
    submittedDate: string;
    problemStatus: string

}

function DormProblem() {
    const navigate = useNavigate();
    let decodedToken: TokenJwtPayload;
    const [isAdmin, setIsAdmin] = useState<boolean>(false);
    const token = getToken();

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
        navigate('/problems/details/' + problemId)
    }

    const handleManageButtonClick = (problemId: number) => {
        navigate('/problems/manage/' + problemId);
    }

    const fetchStudentData = async () => {
        try {
            setLoading(true);
            console.log(isAdmin);
            const response = await fetch(`/api/dorm-problem/get`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });
            const data = await response.json();
            setDormProblems(data.content || data || [])
        } catch (err) {
            console.error('Error fetching dormitory problems');
        } finally {
            setLoading(false);
        }
    }

    const fetchDormProblems = async () => {
        try {
            setLoading(true);
            if (token != null) {
                decodedToken = jwtDecode<TokenJwtPayload>(token);
            } else {
                throw new Error('Cannot get token...')
            }

            console.log(decodedToken['roles'][0]);
            if (decodedToken['roles'][0] == 'ADMIN') {
                setIsAdmin(true);
            } else {
                setIsAdmin(false);
            }
            console.log(isAdmin);
            const response = await fetch(`/api/dorm-problem/get`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });
            const data = await response.json();
            setDormProblems(data.content || data || [])
        } catch (err) {
            console.error('Error fetching dormitory problems');
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchDormProblems();
        
    }, [])

    return (
        <Template
            buttons={[
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' },
                { text: 'Common Rooms', link: '/common-rooms' },
                { text: 'Rooms', link: '/rooms' },
                { text: 'Problems', link: '/problems' },
            ]}
        >

            <div className="flex flex-col items-center p-4">
                <div className="text-center mb-4">
                    <h2 className="text-3xl font-bold text-gray-800">Dormitory Problems</h2>
                </div>
                <div className="flex flex-wrap gap-6 items-center justify-between w-full mb-6">
                    <input
                        type="text"
                        placeholder="Search by description..."
                        value={filters.searchTerm}
                        onChange={(e) => setFilters({ ...filters, searchTerm: e.target.value })}
                        className="w-full md:w-1/3 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                    />
                    <select
                        value={filters.statusFilter}
                        onChange={(e) => setFilters({ ...filters, statusFilter: e.target.value })}
                        className="w-full md:w-1/4 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500 "
                    >
                        <option value="ALL">All Statuses</option>
                        <option value="SUBMITTED">Submitted</option>
                        <option value="RESOLVED">Resolved</option>
                        <option value="REJECTED">Rejected</option>
                    </select>
                    {!isAdmin && (
                        <input
                            type="button"
                            className="w-full md:w-auto bg-gray-500 text-white font-bold py-2 px-4 rounded-lg hover:bg-gray-600 transition"
                            value="Report a problem"
                            onClick={handleButtonClick}
                        />
                    )}
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 w-full">
                    {loading ? (
                        <div className="text-center text-gray-500">Loading problems...</div>
                    ) : filteredProblems.length === 0 ? (
                        <div className="text-center text-gray-500">No problems found matching your criteria</div>
                    ) : (
                        filteredProblems.map((problem) => (
                            <div
                                key={problem.id}
                                className="p-4 bg-white border border-gray-300 rounded-lg shadow-md"
                            >
                                <h3 className="text-lg font-bold text-gray-800">{problem.name}</h3>
                                <p className="text-gray-600">Student id: {problem.studentId}</p>
                                <p
                                    className={`font-semibold ${
                                        problem.problemStatus === "SUBMITTED"
                                            ? "text-yellow-500"
                                            : problem.problemStatus === "RESOLVED"
                                                ? "text-green-500"
                                                : "text-red-500"
                                    }`}
                                >
                                    Status: {problem.problemStatus}
                                </p>
                                <p className="text-gray-600">
                                    Problem date: {new Date(problem.problemDate).toLocaleDateString()}
                                </p>
                                <p className="text-gray-600">
                                    Date submitted: {new Date(problem.submittedDate).toLocaleDateString()}
                                </p>
                                {isAdmin && (
                                    <input
                                        type="button"
                                        className="w-full bg-red-500 text-white font-bold py-2 px-4 rounded-lg hover:bg-red-600 transition mt-2"
                                        value="Manage"
                                        onClick={() => handleManageButtonClick(problem.id)}
                                    />
                                )}
                                <input
                                    type="button"
                                    className="w-full bg-gray-500 text-white font-bold py-2 px-4 rounded-lg hover:bg-gray-600 transition mt-2"
                                    value="View details"
                                    onClick={() => handleViewDetails(problem.id)}
                                />
                            </div>
                        ))
                    )}
                </div>
            </div>
        </Template>
    )
}

export default DormProblem;
