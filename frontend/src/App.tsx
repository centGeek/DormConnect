import { useContext, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './Login/Login.tsx';
import Chat from './Chat/Chat.tsx';
import Home from './Home/Home.tsx';
import EventsCreate from './Events/EventsCreate.tsx';
import Events from './Events/Events.tsx';
import { UserContext } from './Context/UserContext.tsx';
import Rooms from './Rooms/RoomPage.tsx';
import AdminEvents from './Events/AdminEvents.tsx';
import DormFormPage from "./Rooms/DormFormPage.tsx";
import MyAssignments from "./Rooms/components/MyAssignments.tsx";

function App() {
    const userContext = useContext(UserContext);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (userContext) {
            setIsLoading(false); // Ustaw isLoading na false, gdy kontekst zostanie załadowany
        }
    }, [userContext]);

    if (isLoading) {
        return <div>Ładowanie...</div>; // Możesz tu dodać spinner lub inny placeholder
    }

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login />} />
                {userContext?.user ? (
                    <>
                        <Route path="/home" element={<Home />} />
                        <Route path="/chat" element={<Chat />} />
                        <Route path="/events" element={<Events />} />
                        <Route path="/events/create" element={<EventsCreate />} />
                        <Route path="/rooms" element={<Rooms />} />
                        <Route path="/rooms/form" element={<DormFormPage />} />
                        <Route path="/rooms/assignment" element={<MyAssignments />} />

                        {userContext?.user?.roles.includes('ADMIN') && (
                            <Route path="/events/admin/AdminEvents" element={<AdminEvents />} />
                        )}
                    </>
                ) : (
                    <Route path="*" element={<Navigate to="/" replace />} />
                )}
            </Routes>
        </Router>
    );
}

export default App;
