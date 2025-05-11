import { useEffect } from 'react';
import Template from '../Template/Template.tsx';
import './Home.css';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

function Home() {
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
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }, {text:"Common Rooms", link:'/common-rooms'}]}
            footerContent={<p></p>}
        >
            <div className="home-container">
                <h1>Welcome in Dorm Connect</h1>
                <h3>It's an application designed to manage your dormitory</h3>
            </div>
        </Template>
    );
}

export default Home;
