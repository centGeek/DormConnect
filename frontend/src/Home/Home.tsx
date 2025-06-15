import { useEffect } from 'react';
import Template from '../Template/Template.tsx';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import homePageImage from '../assets/homePageImage.png';

function Home() {
    const navigate = useNavigate();

    useEffect(() => {
        const token = Cookies.get('token');
        if (!token) {
            navigate('/');
        }
    }, [navigate]);

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
            <div className="p-4 md:p-8 flex flex-col">
                <div className="flex flex-col items-start gap-6 bg-white p-4 md:p-8 rounded-lg">
                    <h1 className="text-3xl md:text-5xl font-extrabold text-gray-800 text-center w-full">
                        Witaj w <span className="text-black">Dorm Connect!</span>
                    </h1>
                    <div className="flex flex-col md:flex-row items-center gap-6">
                        <h3 className="text-lg md:text-2xl text-gray-700 leading-relaxed text-center md:text-left">
                            Twojej aplikacji do zarządzania domem studenckim. Dorm Connect pozwala na łatwe zarządzanie pokojami,
                            wydarzeniami. Dzięki naszej aplikacji możesz zgłaszać problemy,
                            rezerwować wspólne przestrzenie, czy interpretować regulamin studiów.
                        </h3>
                        <img
                            src={homePageImage}
                            alt="Home Page"
                            className="w-full md:max-w-lg rounded-lg transform hover:scale-105 transition-transform duration-300"
                        />
                    </div>
                </div>
            </div>
        </Template>
    );
}

export default Home;