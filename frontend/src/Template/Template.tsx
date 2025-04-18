import { ReactNode } from 'react';
import './Template.css';

interface TemplateProps {
    headerContent?: ReactNode;
    children: ReactNode;
    footerContent?: ReactNode;
}

function Template({ headerContent, children, footerContent }:TemplateProps) {
    const handleLogout = () => {
        console.log('Logout');
        // Implement your logout logic here
    }
    return (
        <div className="template-container">
            <header className="template-header">
                {headerContent || <h1>Domyślny nagłówek</h1>}
                <button className="logout-button" onClick={handleLogout}>Log out</button>
            </header>
            <main className="template-main">
                {children}
            </main>
            <footer className="template-footer">
                {footerContent || <p>Domyślna stopka</p>}
            </footer>
        </div>
    );
};

export default Template;
