import { ReactNode, useEffect } from 'react';
import LogoPL from '../../public/logo_cale.png';
import { useContext } from 'react';
import { UserContext } from "../Context/UserContext.tsx";
import { useTemperature } from "../Context/TemperatureContext.tsx";

interface TemplateProps {
    children: ReactNode;
    footerContent?: ReactNode;
    buttons?: Button[];
}
interface Button {
    text: string;
    link: string;
}

function Template({ children, footerContent, buttons }: TemplateProps) {
    const userContext = useContext(UserContext);
    const { temperature, loading, error } = useTemperature();

    const handleLogout = async () => {
        try {
            await userContext?.handleLogout();
        } catch (error) {
            console.error('Logout failed:', error instanceof Error ? error.message : error);
        }
    };

    useEffect(() => {
        const updateHeaderTemperature = () => {
            const tempText = loading
                ? 'Ładowanie...'
                : error
                    ? '--°C'
                    : `${temperature}°C`;

            const tooltip =
                !loading && !error && temperature !== null
                    ? temperature > 10
                        ? 'Dobra pogoda na Flanki!'
                        : 'Zła pogoda na Flanki'
                    : '';

            let el = document.querySelector('.header-temperature') as HTMLElement;
            if (!el) {
                el = document.createElement('span');
                el.className = 'header-temperature text-black font-bold ml-auto mr-4';
                const logoutButton = document.querySelector('button.bg-white.text-red-600');
                logoutButton?.parentNode?.insertBefore(el, logoutButton);
            }

            el.innerHTML = tempText;
            el.title = tooltip;
        };

        updateHeaderTemperature();
        const intervalId = setInterval(updateHeaderTemperature, 30000);
        return () => clearInterval(intervalId);
    }, [temperature, loading, error]);

    return (
        <div className="flex flex-col min-h-screen mx-auto max-w-screen-xl w-full min-w-8/12 border border-gray-300 shadow-md rounded-lg mt-1">

            <header className="bg-gray-200 text-white py-2 shadow-md border-gray-700 rounded-t-lg">
                <div className="container mx-auto flex items-center justify-between px-4">
                    <a href="/home" className="flex items-center flex-1">
                        <img
                            src={LogoPL}
                            alt="Logo"
                            className="h-auto max-h-16 w-auto object-contain"
                        />
                    </a>

                    <div className="flex justify-center items-center space-x-4 flex-1">
                        {buttons?.map((button: Button, index: number) => (
                            <a
                                key={index}
                                href={button.link}
                                className="bg-white text-gray-600 px-4 py-2 rounded-lg shadow hover:bg-gray-600 hover:text-white transition whitespace-nowrap"
                            >
                                {button.text}
                            </a>
                        ))}
                    </div>

                    <div className="flex items-center space-x-4 flex-1 justify-end">
                        <span className="header-temperature text-black font-bold"></span>
                        <button
                            className="bg-white text-red-600 px-4 py-2 rounded-lg shadow hover:bg-red-600 transition hover:text-white"
                            onClick={handleLogout}
                        >
                            Wyloguj
                        </button>
                    </div>
                </div>
            </header>


            <main className="flex-grow container mx-auto px-4 py-8">
                {children}
            </main>


            <footer className="bg-gray-800 text-white py-4 rounded-b-lg">
                <div className="container mx-auto text-center">
                    {footerContent || <p>Dorm Connect 2025®</p>}
                </div>
            </footer>
        </div>
    );
}

export default Template;