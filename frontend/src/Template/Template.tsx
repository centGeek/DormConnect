import { ReactNode, useEffect, useState } from 'react';
import LogoPL from '../assets/logo_v1.1.png';
import { useContext } from 'react';
import { UserContext } from "../Context/UserContext.tsx";
import { useTemperature } from "../Context/TemperatureContext.tsx";
import { set } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

interface TemplateProps {
    children: ReactNode;
    footerContent?: ReactNode;
    buttons?: Button[];
}
interface Button {
    text: string;
    link: string;
}

interface DropdownButtonProps {
    name: string;
    url: string;
}

function Template({ children, footerContent, buttons }: TemplateProps) {
    const userContext = useContext(UserContext);
    const { temperature, loading, error } = useTemperature();
    const navigate = useNavigate();
    const [dropdownButtonProps, setDropdownButtonProps] = useState<DropdownButtonProps[]>([])
    const [isOpen, setIsOpen] = useState(false);

    const handleLogout = async () => {
        try {
            await userContext?.handleLogout();
        } catch (error) {
            console.error('Logout failed:', error instanceof Error ? error.message : error);
        }
    };

    const handleDropdownEvent = (url: string) => {
        if (url === '/logout') {
            handleLogout();
        } else {
            navigate(url);
        }
        setIsOpen(false);
    }

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
                        ? 'Good weather for Flanki!'
                        : 'Bad weather for Flanki'
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

        const handleDropdownButtonProps = () => {
            if (userContext?.user?.roles.includes("ADMIN")) {
                setDropdownButtonProps([
                    { name: 'Panel administratora', url: '/admin-panel' },
                    { name: 'Ustawienia konta', url: '/account-settings' },
                    { name: 'Wyloguj', url: '/logout' }
                ]);
            } else if (userContext?.user?.roles.includes("STUDENT")) {
                setDropdownButtonProps([
                    { name: 'Ustawienia konta', url: '/account-settings' },
                    { name: 'Wyloguj', url: '/logout' }
                ]);
            }
        }

        updateHeaderTemperature();
        handleDropdownButtonProps();
        const intervalId = setInterval(updateHeaderTemperature, 30000);
        return () => clearInterval(intervalId);
    }, [temperature, loading, error]);

    return (
        <div className="flex flex-col min-h-screen mx-auto max-w-screen-xl w-full min-w-8/12 border border-gray-300 shadow-md rounded-lg mt-1">

            <header className="bg-gray-200 text-white py-2 shadow-md border-gray-700 rounded-t-lg">
                <div className="container mx-auto flex items-center justify-between px-4">
                    <a href="/home" className="flex items-center">
                        <img
                            src={LogoPL}
                            alt="Logo"
                            className="h-auto max-h-16 w-auto object-contain"
                        />
                    </a>

                    <div className="flex justify-center items-center space-x-4">
                        {buttons?.map((button: Button, index: number) => (
                            <a
                                key={index}
                                href={button.link}
                                className="bg-white text-gray-600 px-4 py-2 rounded-lg shadow hover:bg-gray-600 hover:text-white transition"
                            >
                                {button.text}
                            </a>
                        ))}
                    </div>

                    <div className="relative">
                        {/* Dropdown Button */}
                        <button
                            id="dropdownDefaultButton"
                            onClick={() => setIsOpen(!isOpen)}
                            className="text-white w-65 bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                            type="button"
                        >
                            {userContext?.user?.username}
                            <svg
                                className="w-2.5 h-2.5 ms-3"
                                aria-hidden="true"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 10 6"
                            >
                                <path
                                    stroke="currentColor"
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    d="m1 1 4 4 4-4"
                                />
                            </svg>
                        </button>

                        { }
                        {isOpen && (
                            <div
                                id="dropdown"
                                className="z-10 absolute  bg-white divide-y divide-gray-100 rounded-lg shadow w-65 dark:bg-gray-700"
                            >
                                <ul
                                    className="py-2 text-sm text-gray-700 dark:text-gray-200"
                                    aria-labelledby="dropdownDefaultButton"
                                >
                                    {dropdownButtonProps.map(key => (
                                        <li>
                                            <a
                                                onClick={() => {
                                                    handleDropdownEvent(key.url);
                                                }}
                                                className="hover:cursor-default block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                            >
                                                {key.name}
                                            </a>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}

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