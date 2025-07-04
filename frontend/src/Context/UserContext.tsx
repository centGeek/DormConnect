import React, {createContext, useState, ReactNode, useEffect} from 'react';
import {jwtDecode} from 'jwt-decode';
import Cookies from 'js-cookie';
import axios, { AxiosResponse } from 'axios';
import { useNavigate } from 'react-router-dom';

interface User {
    id: number;
    roles: string[];
    sub: string;
    username: string;
}

interface UserContextProps {
    user: User | null;
    token: string | null;
    handleLogin: (email: string, password: string) => Promise<void>;
    handleLogout: () => Promise<void>;
}

interface DecodedToken {
    roles: string[];
    sub: string;
    id: number;
    username: string;
}

export const UserContext = createContext<UserContextProps | null>(null);

export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(Cookies.get('token') || null);
    const [user, setUser] = useState<User | null>(null);
    const navigate = useNavigate();

    const handleLogin = async (email: string, password: string) => {
        try {
            const response: AxiosResponse = await axios.post(
                '/api/auth/login',
                { email, password },
                {
                    withCredentials: true,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );
            const newToken = response.data.token;
            Cookies.set('token', newToken);
            setToken(newToken);
            const decodedToken: DecodedToken = jwtDecode(newToken);
            setUser({
                id: decodedToken.id,
                roles: decodedToken.roles,
                sub: decodedToken.sub,
                username: decodedToken.username,
            });
            console.log('User logged in:', decodedToken);

            navigate('/home');
        } catch (error) {
            console.error('Login failed:', error instanceof Error ? error.message : error);
            throw new Error('Nieprawidłowy email lub hasło'); // Rzucamy wyjątek
        }
    };

    const handleLogout = async () => {
        try {
            await axios.post(
                '/api/auth/logout',
                {},
                {
                    withCredentials: true,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );
            Cookies.remove('token');
            setToken(null);
            setUser(null);
            navigate('/');
        } catch (error) {
            console.error('Logout failed:', error instanceof Error ? error.message : error);
        }
    };
    useEffect(() => {
        const newToken = Cookies.get('token')
        if (typeof newToken != 'undefined') {
            const decodedToken: DecodedToken = jwtDecode(newToken);
            setUser({
                id: decodedToken.id,
                roles: decodedToken.roles,
                sub: decodedToken.sub,
                username: decodedToken.username
            });
        }
    }, []);



    return (
        <UserContext.Provider value={{ user, token, handleLogin, handleLogout }}>
            {children}
        </UserContext.Provider>
    );
};