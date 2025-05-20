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
    name: string;
    description: string;
    answer: string,
    problemDate: string;
    submittedDate: string;
    problemStatus: string

}

function DormProblemView() {
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
            name: '',
            answer: '',
            problemDate: '',
            submittedDate: '',
            problemStatus: ''
        });

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

                <div className="problem-form">
                    <h3>Problem Details</h3>

                    <div className="form-group">
                        <h4>{dormProblem.name}</h4>
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
                            disabled
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
                            value={dormProblem.answer || ''}
                            readOnly
                            placeholder='...'
                        />
                    </div>

                    <div className="form-group">
                        <label>Problem Date: {new Date(dormProblem.problemDate).toLocaleDateString()}</label>
                        <label>Submitted on: {new Date(dormProblem.submittedDate).toLocaleDateString()}</label>
                    </div>
                </div>
            </div>
        </Template>
    )
}

export default DormProblemView;