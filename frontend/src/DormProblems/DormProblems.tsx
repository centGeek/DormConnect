import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { jwtDecode, JwtPayload } from 'jwt-decode';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import getToken from './GetToken';
import TokenJwtPayload from './TokenJwtPayload';
import './DormProblems.css'

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
    const { state } = useLocation();
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
            { text:"Common Rooms", link:'/common-rooms'}, 
            { text: 'Problems', link: '/problems'}]}
            footerContent={<p></p>}
        >

                <div className="problems-header">
                    <h2>Dormitory Problems</h2>
 
                </div>
            <div className="filter-controls">
                <input
                    type="text"
                    placeholder="Search by description..."
                    value={filters.searchTerm}
                    onChange={(e) => setFilters({...filters, searchTerm: e.target.value})}
                    className="search-input"
                />
                
                <select 
                    value={filters.statusFilter}
                    onChange={(e) => setFilters({...filters, statusFilter: e.target.value})}
                    className="status-filter"
                >
                    <option value='ALL'>All Statuses</option>
                    <option value="SUBMITTED">Submitted</option>
                    <option value="RESOLVED">Resolved</option>
                    <option value="REJECTED">Rejected</option>
                </select>
        { (!isAdmin) && (<input
                        type="button"
                        className="submit-problem-btn"
                        value="Submit a problem"
                        onClick={handleButtonClick}
                    />)}
            </div>

            <div className="dorm-problems-container">


                {loading ? (
                    <div>Loading problems...</div>
                ) : filteredProblems.length === 0 ? (
                    <div>No problems found matching your criteria</div>
                ) : (
                    <div className="problems-grid">
                        {filteredProblems.map(problem => (
                            <div className="problem-card" key={problem.id}>
                                <h3> {problem.name}</h3>

                                <p>Student id: {problem.studentId}</p>
                                <p className={`status-${problem.problemStatus.toLowerCase()}`}>
                                    Status: {problem.problemStatus}
                                </p>
                                <p>Problem date: {new Date(problem.problemDate).toLocaleDateString()}</p>
                                <p>Date submitted: {new Date(problem.submittedDate).toLocaleDateString()}</p>
                                { isAdmin && (<input
                                    type="button"
                                    className="manage-btn"
                                    value="Manage"
                                    onClick={() => handleManageButtonClick(problem.id)}
                                />)}
                                {<input
                                    type='button'
                                    value='View details'
                                    className='view-btn'
                                    onClick= {() => handleViewDetails(problem.id)}
                                />}
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </Template>
    )
}

export default DormProblem;
