import React, { useState } from 'react';
import './Login.css';
import axios, {AxiosResponse} from 'axios';
import {useNavigate} from "react-router-dom";
import Cookies from 'js-cookie';

function Login(){
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const redirection = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response: AxiosResponse = await axios.post('/api/login', {email, password});//tutaj trzeba zmienić ewentualnie
            const token: string = response.data.token;
            Cookies.set('token', token, {maxAge: 600, secure: true, sameSite: 'Strict'});
            console.log('Login successful', response.data);
            redirection('/home')
        }
        catch (error) {
            console.error('Login failed', error instanceof Error ? error.message : error);
            setError('Invalid username or password');
        }
    };

    return (
        <div className="login-container">
            <div className="login-box">
                <h2>Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input
                            type="email"
                            className="form-control"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        {error && <p className="text-danger">{error}</p>} {/* Render error message if exists */}
                    </div>
                    <button type="submit" className="btn btn-primary">Login</button>
                </form>
            </div>
        </div>
    );
}

export default Login;
