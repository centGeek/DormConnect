import {ReactNode} from 'react';
import './Template.css';
import LogoPL from '../assets/Lodz University of Technology_v2.png';
import { useContext } from 'react';
import { UserContext } from '../Context/UserContext.tsx';

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

    return (
        <div className="template-container">
            <div className="template">
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
                    <button className="logout-button" onClick={userContext?.handleLogout}>Log out</button>
                </header>
                <main className="template-main">
                    {children}
                </main>
                <footer className="template-footer">
                    {footerContent || <p>Default footer</p>}
                </footer>
            </div>
        </div>
    );
}

export default Template;