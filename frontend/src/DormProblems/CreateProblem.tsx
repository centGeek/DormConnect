import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';
import axios, { AxiosResponse } from 'axios';

interface DormProblem {
    studentId: number;
    description: string;
    problemDate: string;
    problemStatus: string

}


function DormProblemCreate() {
    const {state} = useLocation();
    const navigate = useNavigate();

    const [problemDesc, setProblemDesc] = useState('');
    const [problemDate, setProblemDate] = useState('');
    const handleButtonClick = () => navigate('/problems');
    const [problemStatus, setProblemStatus] = useState('');

    // const {onSubmit, values} = useForm(
        // handle
    // );

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const createdDormProblem = {
            studentId: 1,
            description: problemDesc,
            problemDate: problemDate,
            problemStatus: "SUBMITTED"
        }

        console.log("submitted");
        console.log(createdDormProblem);

        const response: AxiosResponse = await axios.post(
            'http://localhost:8091/api/dorm-problem/create',
            JSON.stringify(createdDormProblem),
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );

        console.log("Record added successfully");
        console.log(response.data);
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
        <div>Dormitory Problems</div>
        <input type='button' value={"Back"} onClick={handleButtonClick}></input>
        <div>
            <form name='dorm-problem-form' onSubmit={handleSubmit}>
                <label>Problem date</label>
                <input type='date' name='problemDate' value={problemDate} onChange={(e) => setProblemDate(e.target.value)}></input>
                <br/>
                <label>Description</label>
                <input type='text' name='problemDesc' value={problemDesc} placeholder='Problem description' onChange={(e) => setProblemDesc(e.target.value)}></input>
                <br/> 
                <input type='submit' value={'Report the problem'}></input>
            </form>
        </div>
        {/* You might want to display the dormProblems here */}

    </Template>
    )
}

export default DormProblemCreate;