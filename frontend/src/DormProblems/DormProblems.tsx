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

    return (
        <Template
            buttons = {[{ text: 'Home', link: '/home' }, { text: 'Chat', link: '/chat' }, {text: 'Events', link:'/events'}]}
            footerContent={<p></p>}
        >

        </Template>
    )
}

export default DormProblem;