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
    description: string;
    problemDate: string;
    problemStatus: string

}

function DormProblem() {
    const {state} = useLocation();
    const navigate = useNavigate();
    var decodedToken: TokenJwtPayload;

    const token = getToken();
    


    const handleButtonClick = () => navigate('/problems/create');
    const [dormProblems, setDormProblems] = useState<DormProblem[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    const handleManageButtonClick = (problemId: number) => {
        navigate('/problems/manage/' + problemId);
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

            const response = await fetch(`/api/dorm-problem/get`, {
                method: 'GET',
                headers: {
                    'Content-Type':  "application/json",
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
                { text: 'Home', link: '/home' }, 
                { text: 'Chat', link: '/chat' }, 
                { text: 'Events', link: '/events' }
            ]}
            footerContent={<p></p>}
        >
            <div className="dorm-problems-container">
                <div className="problems-header">
                    <h2>Dormitory Problems</h2>
                    <input 
                        type="button" 
                        className="submit-problem-btn" 
                        value="Submit a problem" 
                        onClick={handleButtonClick} 
                    />
                </div>
                
                {loading ? (
                    <div>Loading problems...</div>
                ) : (
                    <div className="problems-grid">
                        {dormProblems.map(problem => (
                            <div className="problem-card" key={problem.id}>
                                <h3>{problem.description}</h3>
                                <p>Problem id: {problem.id}</p>
                                <p>Student id: {problem.studentId}</p>
                                <p className={`status-${problem.problemStatus.toLowerCase()}`}>
                                    Status: {problem.problemStatus}
                                </p>
                                <p>Date: {new Date(problem.problemDate).toLocaleDateString()}</p>
                                <input 
                                    type="button" 
                                    className="manage-btn" 
                                    value="Manage" 
                                    onClick={() => handleManageButtonClick(problem.id)}
                                />
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </Template>
    )
}

export default DormProblem;
