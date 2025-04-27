// src/context/UserContext.tsx
import { createContext, useState, useEffect, ReactNode } from 'react';
import {jwtDecode} from 'jwt-decode';
import Cookies from 'js-cookie';

interface User {
    id: number;
    role: string;
    email: string;
}

interface UserContextProps {
    user: User | null;
    token: string | null;
}
interface DecodedToken {
    role: string;
    email: string;
    id: number;
}

export const UserContext = createContext<UserContextProps | null>(null);

export const UserProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token] = useState<string | null>(Cookies.get('token') || null);

    useEffect(() => {
        if (token) {
            const decoded= jwtDecode<DecodedToken>(token);
            setUser({ role: decoded.role, email: decoded.email, id: decoded.id });
        } else {
            setUser(null);
        }
    }, [token]);



    return (
        <UserContext.Provider value={{ user, token}}>
            {children}
        </UserContext.Provider>
    );
};