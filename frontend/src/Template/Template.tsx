import { ReactNode } from 'react';
import './Template.css';
import LogoPL from '../assets/Lodz University of Technology_v2.png';
import Cookies from "js-cookie";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

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
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true });

            Cookies.remove('token');

            navigate('/login');
        } catch (error) {
            console.error('Logout failed:', error);
        }
    }

    return (
        <div className="template-container">
            <header className="template-header">
                <img src={LogoPL} alt="Logo" className="template-logo" />
                {buttons && (
                    <div className="template-buttons">
                        {buttons.map((button: Button, index: number) => (
                            <a key={index} href={button.link} className="template-button">
                                {button.text}
                            </a>
                        ))}
                    </div>
                )}
                <button className="logout-button" onClick={handleLogout}>Log out</button>
            </header>
            <main className="template-main">
                {children}
            </main>
            <footer className="template-footer">
                {footerContent || <p>Default footer</p>}
            </footer>
        </div>
    );
}

export default Template;