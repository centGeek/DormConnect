import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import TokenJwtPayload from './TokenJwtPayload';
import getToken from './GetToken';
import { jwtDecode } from 'jwt-decode';

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
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms'},
            {text: 'Zgłoś problem', link: '/problems'},
        ]}>
        <div className="max-w-3xl mx-auto p-6 bg-white rounded-lg shadow-md">
            <button
                onClick={handleButtonClick}
                className="flex items-center gap-2 mb-6 px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors duration-200"
            >
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
                </svg>
                Back
            </button>
    
            <form onSubmit={handleFormSubmit} className="space-y-6">
                <h3 className="text-2xl font-bold text-gray-800 border-b pb-2">Problem Details</h3>
    
                <div className="space-y-4">
                    <div className="form-group">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Description:</label>
                        <input
                            type="text"
                            className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-gray-50"
                            value={dormProblem.description}
                            readOnly
                        />
                    </div>
    
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="form-group">
                            <label className="block text-sm font-medium text-gray-700">Problem ID:</label>
                            <div className="mt-1 px-3 py-2 bg-gray-100 rounded-md">{dormProblem.id}</div>
                        </div>
    
                        <div className="form-group">
                            <label className="block text-sm font-medium text-gray-700">Student ID:</label>
                            <div className="mt-1 px-3 py-2 bg-gray-100 rounded-md">{dormProblem.studentId}</div>
                        </div>
                    </div>
    
                    <div className="form-group">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Status:</label>
                        <select
                            className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            value={problemStatus}
                            onChange={(e) => setProblemStatus(e.target.value)}
                        >
                            {dormProblemStatuses.map(status => (
                                <option key={status} value={status}>{problemStatus}</option>
                            ))}
                        </select>
                    </div>
    
                    <div className="form-group">
                        <label className="block text-sm font-medium text-gray-700 mb-1">Answer:</label>
                        <input
                            type="text"
                            className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            name="answer"
                            value={problemAnswer}
                            onChange={(e) => setProblemAnswer(e.target.value)}
                        />
                    </div>
    
                    <div className="form-group">
                        <label className="block text-sm font-medium text-gray-700">Date:</label>
                        <div className="mt-1 px-3 py-2 bg-gray-100 rounded-md">
                            {new Date(dormProblem.problemDate).toLocaleDateString()}
                        </div>
                    </div>
                </div>
    
                <button
                    type="submit"
                    className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
                >
                    Confirm Changes
                </button>
            </form>
        </div>
    </Template>
    )
}

export default DormProblemManage;