// src/context/UserContext.tsx
import { createContext, useState, useEffect, ReactNode } from 'react';
import {jwtDecode} from 'jwt-decode';
import Cookies from 'js-cookie';

interface User {
    role: string;
    email: string;
}

interface UserContextProps {
    user: User | null;
    token: string | null;
    setToken: (token: string) => void;
    refreshToken: () => void;
}
interface DecodedToken {
    role: string;
    email: string;
}

export const UserContext = createContext<UserContextProps | null>(null);

export const UserProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(Cookies.get('token') || null);

    useEffect(() => {
        if (token) {
            const decoded= jwtDecode<DecodedToken>(token);
            setUser({ role: decoded.role, email: decoded.email });
        } else {
            setUser(null);
        }
    }, [token]);

    const refreshToken = async () => {
        try {
            const response = await fetch('/api/refresh-token', { method: 'POST', credentials: 'include' });
            const data = await response.json();
            if (data.token) {
                Cookies.set('token', data.token, { maxAge: 600, secure: true, sameSite: 'Strict' });
                setToken(data.token);
            }
        } catch (error) {
            console.error('Failed to refresh token', error);
        }
    };

    return (
        <UserContext.Provider value={{ user, token, setToken, refreshToken }}>
            {children}
        </UserContext.Provider>
    );
};