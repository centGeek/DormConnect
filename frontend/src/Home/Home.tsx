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
                        ? 'Dobra pogoda na Flanki!'
                        : 'Zła pogoda do Flanek'
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
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms'},
            {text: 'Zgłoś problem', link: '/problems'},
        ]}>
            <div className="justify-items-center">
                <h1 className="text-4xl font-extrabold text-gray-800 text-center mb-4">Witaj w Dorm Connect</h1>
                <h3 className="text-lg text-gray-600 text-center mt-2">Jest to aplikacja do zarządzania domem studenckim</h3>
            </div>
        </Template>
    );
}

export default Home;
