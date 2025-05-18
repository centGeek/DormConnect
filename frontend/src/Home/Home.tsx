import { useEffect } from 'react';
import Template from '../Template/Template.tsx';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useTemperature } from '../Context/TemperatureContext.tsx'; // adjust path

function Home() {
    const navigate = useNavigate();
    const { temperature, loading, error } = useTemperature();

    useEffect(() => {
        // Redirect to login if not authenticated
        const token = Cookies.get('token');
        if (!token) {
            navigate('/');
        }
    }, [navigate]);

    useEffect(() => {
        const updateHeaderTemperature = () => {
            const btn = document.querySelector('a[href="/common-rooms"]');
            if (!btn) return;

            const tempText = loading
                ? 'Loading...'
                : error
                ? '--°C'
                : `${temperature}°C`;

            const tooltip =
                !loading && !error && temperature !== null
                    ? temperature > 10
                        ? 'Good weather for Flanki!'
                        : 'Bad weather for Flanki'
                    : '';

            let el = document.querySelector('.header-temperature') as HTMLElement;
            if (!el) {
                el = document.createElement('span');
                el.className = 'header-temperature';
                btn.parentNode?.insertBefore(el, btn.nextSibling);
            }

            el.innerHTML = tempText;
            el.title = tooltip;
        };

        updateHeaderTemperature();
        const intervalId = setInterval(updateHeaderTemperature, 30000);
        return () => clearInterval(intervalId);
    }, [temperature, loading, error]);

    return (
        <Template
            buttons={[
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' },
                { text: 'Common Rooms', link: '/common-rooms' },
                { text: 'Rooms', link: '/rooms' },
            ]}
        >
            <div className="home-container">
                <h1 className="text-black">Welcome to Dorm Connect</h1>
                <h3>It's an application designed to manage your dormitory</h3>
            </div>
        </Template>
    );
}

export default Home;
