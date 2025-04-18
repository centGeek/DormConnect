//import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './Login/Login.tsx';


function App() {

  return (
      <Router>
          <Routes>
              <Route path="/" element={<Login/>}/>
          </Routes>
      </Router>
  )
}

export default App
