import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';

interface DormProblem {
    id: number;
    studentId: number;
    description: string;
    problemDate: string;
    problemStatus: string

}

function DormProblem() {
    const {state} = useLocation();
    const [dormProblems, setDormProblems] = useState<DormProblem[]>([]);

    const fetchDormProblems = async () => {
        try {
            const response = await fetch(`/api/dorm-problem/get`, {
                method: 'GET',
            });
            const data = await response.json();
            setDormProblems(data.content || data || [])
        } catch (err) {
            console.log(err);
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
        <div>Dormitory Problems</div>
        <div>
            <a href='/problems/create'><input type='button' value={"Submit a problem"}></input></a>
        </div>
        {/* You might want to display the dormProblems here */}
        {dormProblems.map(problem => (
            <div key={problem.id}>
                <h3>{problem.description}</h3>
                <p>Problem id: {problem.id}</p>
                <p>Student id: {problem.studentId}</p>
                <p>Status: {problem.problemStatus}</p>
                <p>Date: {problem.problemDate}</p>
            </div>
        ))}
    </Template>
    )
}

export default DormProblem;