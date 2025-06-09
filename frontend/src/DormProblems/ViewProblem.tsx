import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation, useParams, redirect } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import TokenJwtPayload from './TokenJwtPayload';
import getToken from './GetToken';
import { jwtDecode } from 'jwt-decode';

import { HttpStatusCode } from 'axios';

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
    const context = useContext(UserContext);

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
            console.log(context?.token);
            const fetchUrl = '/api/dorm-problem/problem-statuses';
            const response = await fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${context?.token}`
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
            console.log(fetchUrl);

            const response = await fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${context?.token}`
                },
                credentials: 'include'
            });
            console.log(response);
            if (response.status == HttpStatusCode.InternalServerError) {
                //navigate("/problems")
            }
            return response;


        } catch (err) {
            console.log(err);
        }
    }

    useEffect(() => {
        event?.preventDefault();
        const problem = fetchDormProblem();
        console.log(problem)
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
    <div className="max-w-3xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <button
            className="mb-6 px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition duration-300 flex items-center"
            onClick={handleButtonClick}
        >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
            </svg>
            Back
        </button>

        <div className="space-y-6">
            <h3 className="text-2xl font-bold text-gray-800 border-b pb-2">Problem Details</h3>

            <div className="space-y-4">
                <h4 className="text-xl font-semibold text-gray-700">{dormProblem.name}</h4>
                
                <div className="space-y-2">
                    <label className="block text-gray-700 font-medium">Description:</label>
                    <input
                        type="text"
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50"
                        value={dormProblem.description}
                        readOnly
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="space-y-2">
                        <label className="block text-gray-700 font-medium">Problem ID:</label>
                        <div className="px-4 py-2 bg-gray-100 rounded-lg">{dormProblem.id}</div>
                    </div>

                    <div className="space-y-2">
                        <label className="block text-gray-700 font-medium">Student ID:</label>
                        <div className="px-4 py-2 bg-gray-100 rounded-lg">{dormProblem.studentId}</div>
                    </div>
                </div>

                <div className="space-y-2">
                    <label className="block text-gray-700 font-medium">Status:</label>
                    <select
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 cursor-not-allowed"
                        value={dormProblem.problemStatus}
                        disabled
                    >
                        <option value={dormProblem.problemStatus}>{dormProblem.problemStatus}</option>
                    </select>
                </div>

                <div className="space-y-2">
                    <label className="block text-gray-700 font-medium">Answer:</label>
                    <input
                        type="text"
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50"
                        value={dormProblem.answer || ''}
                        readOnly
                        placeholder='No answer provided...'
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
                    <div className="space-y-2">
                        <label className="block text-gray-700 font-medium">Problem Date:</label>
                        <div className="px-4 py-2 bg-gray-100 rounded-lg">
                            {new Date(dormProblem.problemDate).toLocaleDateString()}
                        </div>
                    </div>
                    <div className="space-y-2">
                        <label className="block text-gray-700 font-medium">Submitted on:</label>
                        <div className="px-4 py-2 bg-gray-100 rounded-lg">
                            {new Date(dormProblem.submittedDate).toLocaleDateString()}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</Template>
    )
}

export default DormProblemView;