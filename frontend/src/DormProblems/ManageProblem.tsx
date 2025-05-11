import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import { parseJwt } from '../JWT/JWTDecoder';
import { UserContext } from '../Context/UserContext';
import Template from '../Template/Template';


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
    const {state} = useLocation();
    const pageData = useParams();
    const navigate = useNavigate();


    const handleButtonClick = () => navigate('/problems');
    const [dormProblem, setDormProblem] = useState<DormProblem>(
        {
            id: 0, 
            description: '', 
            studentId: 0,
            answer: '',
            problemDate: '',
            submittedDate:'',
            problemStatus: ''
         });

    const handleFormSubmit = () => {
        console.log("form submitted");
    }

    const fetchDormProblem = async () => {
        try {
            console.log(pageData.problemId);
            const fetchUrl = '/api/dorm-problem/get/' + pageData.problemId;
            const response = await fetch(fetchUrl, {
                method: 'GET',
            });
            const data = await response.json();
            console.log(data)
            
            setDormProblem(data || [])
        } catch (err) {
            console.log(err);
        }
    }

    useEffect(() => {
        event?.preventDefault();
        fetchDormProblem();
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
        <div>
            <input type='button' value={"Back"} onClick={handleButtonClick}></input>
        </div>
         
            <form action={handleFormSubmit}>

                <h3>{dormProblem.description}</h3>
                <label>Description: </label>
                <br/>
                <input type='text' value={dormProblem.description} readOnly></input>
                
                <p>Problem id: {dormProblem.id}</p>
                <p>Student id: {dormProblem.studentId}</p>
                <p>Status: {dormProblem.problemStatus}</p>
                <p>Date: {dormProblem.problemDate}</p>
            </form>
    </Template>
    )
}

export default DormProblemManage;