import { useEffect } from 'react';
import Template from '../Template/Template.tsx';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useTemperature } from '../Context/TemperatureContext.tsx';
import homePageImage from '../assets/homePageImage.png';

function Home() {
    const navigate = useNavigate();
    const { temperature, loading, error } = useTemperature();

    useEffect(() => {
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
            <div className="p-8 flex flex-col">
                <div className="flex flex-col items-start gap-6 bg-white p-8 rounded-lg">
                    <h1 className="text-5xl font-extrabold text-gray-800 text-center w-full">
                        Witaj w <span className="text-black">Dorm Connect!</span>
                    </h1>
                    <div className="flex flex-row items-center gap-6">
                        <h3 className="text-2xl text-gray-700 leading-relaxed">
                            Twojej aplikacji do zarządzania domem studenckim. Dorm Connect pozwala na łatwe zarządzanie pokojami,
                            wydarzeniami. Dzięki naszej aplikacji możesz zgłaszać problemy,
                            rezerwować wspólne przestrzenie, czy interpretować regulamin studiów.
                        </h3>
                        <img
                            src={homePageImage}
                            alt="Home Page"
                            className="max-w-lg rounded-lg transform hover:scale-105 transition-transform duration-300"
                        />
                    </div>
                </div>
            </div>
        </Template>
    );
}

export default Home;
