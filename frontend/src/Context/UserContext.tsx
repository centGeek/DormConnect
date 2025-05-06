import React, {createContext, useState, ReactNode, useEffect} from 'react';
import {jwtDecode} from 'jwt-decode';
import Cookies from 'js-cookie';
import axios, { AxiosResponse } from 'axios';
import { useNavigate } from 'react-router-dom';

interface User {
    id: number;
    roles: string[];
    sub: string;
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
}

export const UserContext = createContext<UserContextProps | null>(null);

export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(Cookies.get('token') || null);
    const [user, setUser] = useState<User | null>(null);
    const navigate = useNavigate();

    const handleLogin = async (email: string, password: string) => {
        try {
            const response: AxiosResponse = await axios.post(
                'http://localhost:8091/api/auth/login',
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
            });
            console.log('User logged in:', decodedToken);

            navigate('/home');
        } catch (error) {
            console.error('Login failed:', error instanceof Error ? error.message : error);
        }
    };

    const handleLogout = async () => {
        try {
            await axios.post(
                'http://localhost:8091/api/auth/logout',
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
        const decodedToken: DecodedToken = jwtDecode(token as string);
        setUser({
            id: decodedToken.id,
            roles: decodedToken.roles,
            sub: decodedToken.sub,
        });
        console.log('User logged in:', user);
    }, []);


    return (
        <UserContext.Provider value={{ user, token, handleLogin, handleLogout }}>
            {children}
        </UserContext.Provider>
    );
};