import Template from "../Template/Template";
import {useContext, useEffect, useState} from "react";
import { buttons } from "../ReusableComponents/buttons.ts";
import {UserContext} from "../Context/UserContext.tsx";
import PopUpChangePassword from "./PopUpChangePassword.tsx";
import Cookies from "js-cookie";
import PopUpChangeUsername from "./PopUpChangeUsername.tsx";

export default function AccountSettingsPanel() {
    const userContext = useContext(UserContext);
    const [name, setName] = useState<string>('');
    const [surname, setSurname] = useState<string>('');
    const [username, setUsername] = useState<string>('');
    const [isChangePasswordOpen, setIsChangePasswordOpen] = useState<boolean>(false);
    const [isChangeUsernameOpen, setIsChangeUsernameOpen] = useState<boolean>(false);

    // @ts-ignore
    useEffect(() => {
        if (userContext?.user) {
            fetchNameSurname();
            fetchUsername();
        }
    }, [userContext?.user]);

    const fetchNameSurname = async () => {
        try {
            const response = await fetch(`/api/users/get/name-surname/${userContext?.user?.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': 'Bearer ' + Cookies.get('token')
                }
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            setName(data.name);
            setSurname(data.surname);
        } catch (error) {
            console.error("Error fetching name and surname:", error);
        }
    };

    const fetchUsername = async () => {
        try {
            const response = await fetch(`/api/users/get/username/${userContext?.user?.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': 'Bearer ' + Cookies.get('token')
                }
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const username = await response.text();
            setUsername(username);
        } catch (error) {
            console.error("Error fetching username:", error);
        }
    };

    if (!userContext?.user) {
        return <div className="flex justify-center items-center h-screen">Ładowanie...</div>;
    }

    return (
        <Template buttons={buttons}>
            <div className="max-w-4xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
                <h1 className="text-2xl font-bold mb-6 text-center">Ustawienia konta</h1>
                <div className="space-y-4">
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <label className="font-medium text-gray-700">Imię:</label>
                        <p className="text-gray-600">{name}</p>
                    </div>
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <label className="font-medium text-gray-700">Nazwisko:</label>
                        <p className="text-gray-600">{surname}</p>
                    </div>
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <label className="font-medium text-gray-700">Email:</label>
                        <p className="text-gray-600">{userContext?.user?.sub}</p>
                    </div>
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <label className="font-medium text-gray-700">Nazwa użytkownika:</label>
                        <p className="text-gray-600">{username}</p>
                    </div>
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <label className="font-medium text-gray-700">Rola:</label>
                        <p className="text-gray-600">{userContext?.user?.roles[0]?.toLowerCase()?.replace(/^\w/, (c) => c.toUpperCase())}</p>
                    </div>
                </div>
                <div className="mt-8 flex flex-col md:flex-row md:justify-between space-y-4 md:space-y-0">
                    <button className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition" onClick={() => setIsChangePasswordOpen(true)}>
                        Zmień hasło
                    </button>
                    <button className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition" onClick={() => setIsChangeUsernameOpen(true)}>
                        Zmień nazwę użytkownika
                    </button>
                </div>
            </div>
            {isChangePasswordOpen && <PopUpChangePassword onClose={() => setIsChangePasswordOpen(false)} />}
            {isChangeUsernameOpen && <PopUpChangeUsername onClose={() => setIsChangeUsernameOpen(false)} />}
        </Template>
    );
}