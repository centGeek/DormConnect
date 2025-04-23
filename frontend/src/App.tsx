//import React from 'react';
import {  Route, Routes } from 'react-router-dom';
import Login from './Login/Login.tsx';
import Chat from './Chat/Chat';
import Home from './Home/Home.tsx';
import {UserProvider} from "./Context/UserContext.tsx";

function App() {

  return (
      <UserProvider>
          <Routes>
              <Route path="/" element={<Login/>}/>
              <Route path="/home" element={<Home/>}/>
              <Route path="/chat" element={<Chat/>}/>
          </Routes>
      </UserProvider>
  )
}

export default App
