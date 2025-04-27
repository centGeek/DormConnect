import { useState, useEffect } from 'react';
import Template from '../Template/Template.tsx';
import './Home.css';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

function Home() {
    const [counter, setCounter] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const token = Cookies.get('token');
        if (!token) {
             console.log('Token value:', token); 
            navigate('/');
        }
    }, [navigate]);

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="home-container">
                <h1>Welcome in Dorm Connect</h1>
                <h3>It's an application designed to manage your dormitory</h3>
                <h4>You can play with this counter below</h4>
                <button className="home-button" onClick={() => setCounter(counter + 1)}>{counter}</button>
            </div>
        </Template>
    );
}

export default Home;
