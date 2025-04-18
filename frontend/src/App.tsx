//import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './Login/Login.tsx';
import Chat from './Chat/Chat';

function App() {

  return (
      <Router>
          <Routes>
              <Route path="/" element={<Login/>}/>
              <Route path="/chat" element={<Chat/>}/>
          </Routes>
      </Router>
  )
}

export default App
