import {createRoot} from 'react-dom/client'
import 'bootstrap/dist/css/bootstrap.min.css';
import App from './App.tsx'
import {UserProvider} from "./Context/UserContext.tsx";
import {StrictMode} from "react";
import {BrowserRouter as Router} from "react-router-dom";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <Router>
            <UserProvider>
                <App/>
            </UserProvider>
        </Router>
    </StrictMode>
)
