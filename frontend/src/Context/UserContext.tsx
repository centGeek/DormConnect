// src/context/UserContext.tsx
import React, { createContext, useState, useEffect, ReactNode } from 'react';
import {jwtDecode} from 'jwt-decode';
import Cookies from 'js-cookie';
import { HttpStatusCode } from 'axios';

interface User {
    id: number;
    roles: string[];
    sub: string;
}

interface UserContextProps {
    user: User | null;
    token: string | null;
}
interface DecodedToken {
    roles: string[];
    sub: string;
    id: number;
}

export const UserContext = createContext<UserContextProps | null>(null);


export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) =>  {
    const [token] = useState<string | null>(Cookies.get('token') || null);

    // user - current state, setUser - change state of the user
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        if (token) {
            try {
                const decodedToken: DecodedToken = jwtDecode(token);
                const user: User = {
                    id: decodedToken.id,
                    roles: decodedToken.roles,
                    sub: decodedToken.sub
                }
                setUser(user);
            }
                // eslint-disable-next-line @typescript-eslint/no-unused-vars
            catch(error) {
                console.error(HttpStatusCode.InternalServerError);
                setUser(null);
            }
        } else {
            setUser(null);
        }
    }, [token]);


    return (
        <UserContext.Provider value={{user, token}}>
            {children}
        </UserContext.Provider>
    );
};