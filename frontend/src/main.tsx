import {createRoot} from 'react-dom/client'
import App from './App.tsx'
import {UserProvider} from "./Context/UserContext.tsx";
import {StrictMode} from "react";
import {BrowserRouter as Router} from "react-router-dom";
import './index.css';
createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <Router>
            <UserProvider>
                <App/>
            </UserProvider>
        </Router>
    </StrictMode>
)
