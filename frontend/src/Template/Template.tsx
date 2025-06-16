import { ReactNode, useEffect, useState } from 'react';
import LogoPL from '/logo_cale.png';
import { useContext } from 'react';
import { UserContext } from "../Context/UserContext.tsx";
import { useTemperature } from "../Context/TemperatureContext.tsx";
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
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
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
            const btn = document.querySelector('a[href="/problems"]');
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
                el.className = 'header-temperature text-gray-800 font-bold mr-auto';
                btn.parentNode?.insertBefore(el, btn.nextSibling); // Poprawiono błąd
            }

            el.innerHTML = tempText;
            el.title = tooltip;
        };

        updateHeaderTemperature();
        const intervalId = setInterval(updateHeaderTemperature, 30000);
        return () => clearInterval(intervalId);
    }, [temperature, loading, error]);
    useEffect(() => {
        const handleDropdownButtonProps = () => {
            if ((userContext?.user?.roles.includes("ADMIN")|| userContext?.user?.roles.includes("MANAGER"))) {
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


        handleDropdownButtonProps();
    }, [temperature, loading, error, userContext?.user?.roles]);

    return (
        <div className="flex flex-col min-h-screen mx-auto max-w-screen-xl w-full min-w-8/12 border border-gray-300 shadow-md rounded-lg">
            <header className="bg-gray-200 text-white py-2 shadow-md border-gray-700 rounded-t-lg">
                <div className="container mx-auto flex items-center justify-between px-4">
                    <a href="/home" className="flex items-center">
                        <img
                            src={LogoPL}
                            alt="Logo"
                            className="h-auto max-h-16 w-auto object-contain"
                        />
                    </a>

                    {/* Hamburger na mobile */}
                    <button
                        className="md:hidden flex flex-col justify-center items-center"
                        onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                        aria-label="Otwórz menu"
                    >
                        <span className="block w-6 h-0.5 bg-gray-600 mb-1"></span>
                        <span className="block w-6 h-0.5 bg-gray-600 mb-1"></span>
                        <span className="block w-6 h-0.5 bg-gray-600"></span>
                    </button>

                    {/* Menu na desktop */}
                    <div className="hidden md:flex justify-center items-center space-x-4">
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

                    {/* Dropdown i user */}
                    <div className="relative hidden md:block">
                        <button
                            id="dropdownDefaultButton"
                            onClick={() => setIsOpen(!isOpen)}
                            className="text-white w-fit bg-gray-500 hover:bg-gray-600 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center "
                            type="button"
                        >
                            {userContext?.user?.username}
                            {isOpen ? (
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
                                        d="m1 5 4-4 4 4"
                                    />
                                </svg>
                            ) : (
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
                            )}
                        </button>
                        {isOpen && (
                            <div
                                id="dropdown"
                                className="hidden md:block z-10 absolute border border-gray-600  bg-gray-500 divide-y  rounded-lg w-fit "
                            >
                                <ul className="py-2 text-sm ">
                                    {dropdownButtonProps.map(key => (
                                        <li key={key.url}>
                                            <a
                                                onClick={() => {
                                                    handleDropdownEvent(key.url);
                                                }}
                                                className="hover:cursor-default block px-4 py-2 hover:bg-gray-400"
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

                {mobileMenuOpen && (
                    <div
                        className="md:hidden flex flex-col items-center bg-gray-200 py-2 space-y-2 shadow-lg rounded-b-lg transition-all duration-300 ease-in-out opacity-100 translate-y-0 animate-fade-in"
                        style={{
                            animation: "fadeSlideDown 0.3s ease"
                        }}
                    >
                        {buttons?.map((button: Button, index: number) => (
                            <a
                                key={index}
                                href={button.link}
                                className="bg-white text-gray-600 px-4 py-2 rounded-lg shadow hover:bg-gray-600 hover:text-white transition w-11/12 text-center"
                                onClick={() => setMobileMenuOpen(false)}
                            >
                                {button.text}
                            </a>
                        ))}
                        {dropdownButtonProps.map((key) => (
                            <a
                                key={key.url}
                                className="bg-white text-gray-600 px-4 py-2 rounded-lg shadow hover:bg-gray-600 hover:text-white transition w-11/12 text-center"
                                onClick={() => {
                                    handleDropdownEvent(key.url);
                                    setMobileMenuOpen(false);
                                }}
                            >
                                {key.name}
                            </a>
                        ))}
                    </div>
                )}
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