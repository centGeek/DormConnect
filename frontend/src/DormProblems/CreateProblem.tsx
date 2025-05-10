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



function DormProblemCreate() {
    const {state} = useLocation();



    // const {onSubmit, values} = useForm(
        // handle
    // );

    async function handleSubmit() {

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
        <div>
            <form name='dorm-problem-form'>
                <label>Problem date</label>
                <input type='date' name='problemDate'></input>
                <br/>
                <label>Description</label>
                <input type='text' name='problemDesc' placeholder='Problem description'></input>
                <br/>
                <input type='submit' value={'Report the problem'}></input>
            </form>
        </div>
        {/* You might want to display the dormProblems here */}

    </Template>
    )
}

export default DormProblemCreate;