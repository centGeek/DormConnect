//import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Login from './Login/Login.tsx';
import Chat from './Chat/Chat.tsx';
import Home from './Home/Home.tsx';
import EventsCreate from './Events/EventsCreate.tsx';
import Events from './Events/Events.tsx';
import {UserProvider} from "./Context/UserContext.tsx";
import Rooms from "./Rooms/RoomPage.tsx";
import AdminEvents from './Events/AdminEvents.tsx';

function App() {

  return (
      <UserProvider>
          <Router>
              <Routes>
                  <Route path="/" element={<Login/>}/>
                  <Route path="/home" element={<Home/>}/>
                  <Route path="/chat" element={<Chat/>}/>
                  <Route path="/events" element={<Events/>}/>
                  <Route path="/events/create" element={<EventsCreate/>}/>
                  <Route path="/rooms" element={<Rooms/>}/>
                  <Route path="/admin/events" element={<AdminEvents />} />
              </Routes>
          </Router>
      </UserProvider>
  )
}

export default App
