import {ReactNode} from 'react';
import './Template.css';
import LogoPL from '../assets/Lodz University of Technology_v2.png';
import { useNavigate } from 'react-router-dom';
import axios, {AxiosResponse} from 'axios';

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
            const response : AxiosResponse = await axios.post(
                'http://localhost:8091/api/auth/logout',
                {},
                {
                    withCredentials: true,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )
            console.log('Logout successful', response.data);
            navigate('/');
        } catch (error) {
            console.error('Logout failed:', error instanceof Error ? error.message : error);
        }
    }

    return (
        <div className="template-container">
            <header className="template-header">
                <a href={"/home"}><img src={LogoPL} alt="Logo" className="template-logo" /></a>
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