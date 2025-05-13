import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import TokenJwtPayload from './TokenJwtPayload';
import getToken from './GetToken';
import { jwtDecode } from 'jwt-decode';
import './ManageProblem.css'

interface DormProblem {
    id: number;
    studentId: number;
    description: string;
    answer: string,
    problemDate: string;
    submittedDate: string;
    problemStatus: string

}

function DormProblemManage() {
    const { state } = useLocation();
    const pageData = useParams();
    const navigate = useNavigate();
    const [problemAnswer, setProblemAnswer] = useState('');
    const [problemStatus, setProblemStatus] = useState('');

    var decodedToken: TokenJwtPayload;
    const token = getToken();

    const [dormProblemStatuses, setDormProblemStatuses] = useState<string[]>([]);
    const handleButtonClick = () => navigate('/problems');
    const [dormProblem, setDormProblem] = useState<DormProblem>(
        {
            id: 0,
            description: '',
            studentId: 0,
            answer: '',
            problemDate: '',
            submittedDate: '',
            problemStatus: ''
        });

    const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log("form submitted");
        if (token != null) {
            try {
                decodedToken = jwtDecode<TokenJwtPayload>(token);
                const updatedProblem = {
                    id: dormProblem.id,
                    studentId: dormProblem.studentId,
                    description: dormProblem.description,
                    problemStatus: problemStatus,
                    answer: problemAnswer
                }
    
                const response = await fetch(`/api/dorm-problem/update/${dormProblem.id}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(updatedProblem),
                    credentials: 'include'
                }); 

                navigate('/problems');  
            } catch(err) {
                console.error(err);
            }


        } else {
            throw new Error('Cannot get token...')
        }


    }

    const fetchProblemStatuses = async () => {
        try {
            console.log(pageData.problemId);
            const fetchUrl = '/api/dorm-problem/problem-statuses';
            const response = await fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });
            const data = await response.json();
            console.log(data)

            setDormProblemStatuses(data || [])
            console.log(dormProblemStatuses);


        } catch (err) {
            console.log(err);
        }
    }

    const fetchDormProblem = async () => {
        try {
            console.log(pageData.problemId);
            const fetchUrl = '/api/dorm-problem/get/' + pageData.problemId;
            const response = await fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include'
            });
            const data = await response.json();
            console.log(data)

            setDormProblem(data || [])
            setProblemStatus(dormProblem.problemStatus);
            setProblemAnswer(dormProblem.answer || '');


        } catch (err) {
            console.log(err);
        }
    }

    useEffect(() => {
        event?.preventDefault();
        fetchDormProblem();
        fetchProblemStatuses();
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
            <div className="manage-problem-container">
                <input
                    type="button"
                    className="back-btn"
                    value="Back"
                    onClick={handleButtonClick}
                />

                <form className="problem-form" onSubmit={handleFormSubmit}>
                    <h3>Problem Details</h3>

                    <div className="form-group">
                        <label>Description:</label>
                        <input
                            type="text"
                            className="form-control"
                            value={dormProblem.description}
                            readOnly
                        />
                    </div>

                    <div className="form-group">
                        <label>Problem ID: {dormProblem.id}</label>
                    </div>

                    <div className="form-group">
                        <label>Student ID: {dormProblem.studentId}</label>
                    </div>

                    <div className="form-group">
                        <label>Status:</label>
                        <select
                            className="status-select"
                            value={problemStatus}
                            onChange={(e) => setProblemStatus(e.target.value)}
                        >
                            {dormProblemStatuses.map(status => (
                                <option key={status} value={status}>{status}</option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Answer:</label>
                        <input
                            type="text"
                            className="form-control"
                            name="answer"
                            value={problemAnswer}
                            onChange={(e) => setProblemAnswer(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Date: {new Date(dormProblem.problemDate).toLocaleDateString()}</label>
                    </div>

                    <input
                        type="submit"
                        className="submit-btn"
                        value="Confirm Changes"
                    />
                </form>
            </div>
        </Template>
    )
}

export default DormProblemManage;