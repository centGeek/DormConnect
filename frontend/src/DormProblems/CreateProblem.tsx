import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import axios, { AxiosResponse } from 'axios';
import getToken from './GetToken';
import TokenJwtPayload from './TokenJwtPayload';
import { jwtDecode } from 'jwt-decode';

interface DormProblem {
    studentId: number;
    name: string;
    description: string;
    problemDate: string;
    problemStatus: string

}


function DormProblemCreate() {
    const { state } = useLocation();
    const navigate = useNavigate();
    var decodedToken: TokenJwtPayload;
    const [problemDesc, setProblemDesc] = useState('');
    const [problemDate, setProblemDate] = useState('');
    const [problemName, setProblemName] = useState('');
    const handleButtonClick = () => navigate('/problems');
    const [problemStatus, setProblemStatus] = useState('');
    // const {onSubmit, values} = useForm(
    // handle
    // );
    const token = getToken();

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (token != null) {
            decodedToken = jwtDecode<TokenJwtPayload>(token);
        } else {
            throw new Error('Cannot get token...')
        }
        const createdDormProblem = {
            studentId: decodedToken['id'],
            name: problemName,
            description: problemDesc,
            problemDate: problemDate,
            problemStatus: "SUBMITTED"
        }

        console.log("submitted");
        console.log(createdDormProblem);

        const response = await fetch('/api/dorm-problem/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(createdDormProblem),
            credentials: 'include'
        });


        console.log("Record added successfully");
        console.log(response.status);
        navigate('/problems')
    };



    useEffect(() => {
    }, [])

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms'},
            {text: 'Zgłoś problem', link: '/problems'},
        ]}>
    <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <button
            className="mb-6 px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition duration-300 flex items-center"
            onClick={handleButtonClick}
        >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
            </svg>
            Powrót
        </button>

        <h2 className="text-3xl font-bold text-gray-800 mb-8 text-center">Zgłoś Problem</h2>

        <form className="space-y-6" name="dorm-problem-form" onSubmit={handleSubmit}>
            <div className='space-y-2'>
                <label className="block text-gray-700 font-medium">Nazwa Problemu:</label>
                <input
                    type='text'
                    name='name'
                    value={problemName}
                    onChange={(e) => setProblemName(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500"
                    required
                />
            </div>

            <div className="space-y-2">
                <label className="block text-gray-700 font-medium">Problem Date:</label>
                <input
                    type="date"
                    name="problemDate"
                    value={problemDate}
                    onChange={(e) => setProblemDate(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500"
                    required
                />
            </div>

            <div className="space-y-2">
                <label className="block text-gray-700 font-medium">Description:</label>
                <textarea
                    name="problemDesc"
                    value={problemDesc}
                    placeholder="Describe the problem in detail"
                    onChange={(e) => setProblemDesc(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500 min-h-[120px]"
                    required
                />
            </div>

            <button
                type="submit"
                className="w-full bg-gray-800 text-white font-bold py-3 px-4 rounded-lg hover:bg-gray-700 transition duration-300"
            >
                Report Problem
            </button>
        </form>
    </div>
</Template>
    )
}

export default DormProblemCreate;