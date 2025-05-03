
import { useContext } from 'react';
import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom';
import Login from './Login/Login.tsx';
import Chat from './Chat/Chat.tsx';
import Home from './Home/Home.tsx';
import EventsCreate from './Events/EventsCreate.tsx';
import Events from './Events/Events.tsx';
import { UserProvider, UserContext } from './Context/UserContext.tsx';
import Rooms from "./Rooms/Rooms.tsx";
import AdminEvents from './Events/AdminEvents.tsx';

function App() {

    const userContext = useContext(UserContext);
    console.log('UserContext:', userContext);
    console.log('UserContext:', userContext?.user?.role);
  return (
      <UserProvider>
          <Router>
              <Routes>
                  <Route path="/" element={<Login />} />

                  {/* Ścieżki dla zalogowanych użytkowników */}
                  {userContext?.user && (
                      <>
                          <Route path="/home" element={<Home />} />
                          <Route path="/chat" element={<Chat />} />
                          <Route path="/events" element={<Events />} />
                          <Route path="/events/create" element={<EventsCreate />} />
                          <Route path="/rooms" element={<Rooms />} />
                      </>
                  )}

                  {/* Ścieżki tylko dla administratora */}
                  {userContext?.user?.role === 'ADMIN' && (
                      <Route path="/admin/events" element={<AdminEvents />} />
                  )}

                  {/* Przekierowanie dla niezalogowanych użytkowników */}
                  {!userContext?.user && (
                      <Route path="*" element={<Navigate to="/" replace />} />
                  )}
              </Routes>
          </Router>
      </UserProvider>
  )
}

export default App
