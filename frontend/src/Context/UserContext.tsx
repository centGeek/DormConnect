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

export const UserProvider = () => {
    const [token] = useState<string | null>(Cookies.get('token') || null);

    useEffect(() => {
        if (token) {
            try {
                const decodedToken: DecodedToken = jwtDecode(token);
                const user: User = (decodedToken.id, decodedToken.role, decodedToken.email);
            }
        } else {
            setUser(null);
        }
    }, [token]);



    return (
        <UserContext.Provider>
        </UserContext.Provider>
    );
};