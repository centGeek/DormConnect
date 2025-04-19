//import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './Login/Login.tsx';
import Chat from './Chat/Chat';
import Home from './Home/Home.tsx';

function App() {

  return (
      <Router>
          <Routes>
              <Route path="/" element={<Login/>}/>
              <Route path="/home" element={<Home/>}/>
              <Route path="/chat" element={<Chat/>}/>
          </Routes>
      </Router>
  )
}

export default App
