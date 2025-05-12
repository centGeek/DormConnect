import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import axios, { AxiosResponse } from 'axios';
import getToken from './GetToken';
import TokenJwtPayload from './TokenJwtPayload';
import { jwtDecode } from 'jwt-decode';
import './CreateProblem.css'

interface DormProblem {
    studentId: number;
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
        <Template
            buttons={[
                { text: 'Home', link: '/home' },
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' }
            ]}
            footerContent={<p></p>}
        >
            <div className="create-problem-container">
                <input
                    type="button"
                    className="create-back-btn"
                    value="Back"
                    onClick={handleButtonClick}
                />

                <h2>Report New Problem</h2>

                <form className="create-form" name="dorm-problem-form" onSubmit={handleSubmit}>
                    <div className="form-row">
                        <label>Problem Date:</label>
                        <input
                            type="date"
                            name="problemDate"
                            value={problemDate}
                            onChange={(e) => setProblemDate(e.target.value)}
                        />
                    </div>

                    <div className="form-row">
                        <label>Description:</label>
                        <input
                            type="text"
                            name="problemDesc"
                            value={problemDesc}
                            placeholder="Describe the problem in detail"
                            onChange={(e) => setProblemDesc(e.target.value)}
                        />
                    </div>

                    <input
                        type="submit"
                        className="report-btn"
                        value="Report Problem"
                    />
                </form>
            </div>
        </Template>
    )
}

export default DormProblemCreate;