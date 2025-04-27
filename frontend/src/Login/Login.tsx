import React, { useState } from 'react';
import './Login.css';
import axios, { AxiosResponse } from 'axios';
import { useNavigate } from 'react-router-dom';

function Login() {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const response: AxiosResponse = await axios.post(
                'http://localhost:8091/api/auth/login',
                { email, password },
                {
                    withCredentials: true, // Upewnij się, że ciasteczka będą wysyłane
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            );
            console.log('Login successful', response.data);
            // Poczekaj na zapisanie ciasteczka, a potem przekieruj
            navigate('/home');  // Jeśli logowanie zakończyło się sukcesem
        } catch (error) {
            console.error('Login failed', error instanceof Error ? error.message : error);
            setError('Invalid email or password');
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
                        {error && <p className="text-danger">{error}</p>}
                    </div>
                    <button type="submit" className="btn btn-primary">Login</button>
                </form>
            </div>
        </div>
    );
}

export default Login;