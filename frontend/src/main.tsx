import { createRoot } from 'react-dom/client'
import 'bootstrap/dist/css/bootstrap.min.css';
import App from './App.tsx'
import {UserProvider} from "./Context/UserContext.tsx";

createRoot(document.getElementById('root')!).render(
      <UserProvider>
        <App />
      </UserProvider>
,
)
