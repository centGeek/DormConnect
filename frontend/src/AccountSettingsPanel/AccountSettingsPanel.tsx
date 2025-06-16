import Template from "../Template/Template";
import {useContext, useEffect, useState} from "react";
import { buttons } from "../ReusableComponents/buttons.ts";
import {UserContext} from "../Context/UserContext.tsx";

export default function AccountSettingsPanel() {
    const userContext = useContext(UserContext);
    const [name, setName] = useState<string>('');
    const [surname, setSurname] = useState<string>('');

    const fetchNameSurname = async () => {
        try {
            console.log(userContext?.user?.id);
            const response = await fetch(`/api/users/get/name-surname/${userContext?.user?.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': `Bearer ${userContext?.token}`
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
    }
    useEffect(() => {
        fetchNameSurname()
    }, []);
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
                        <p className="text-gray-600">{userContext?.user?.username}</p>
                    </div>
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <label className="font-medium text-gray-700">Rola:</label>
                        <p className="text-gray-600">{userContext?.user?.roles[0]?.toLowerCase()?.replace(/^\w/, (c) => c.toUpperCase())}</p>
                    </div>
                </div>
                <div className="mt-8 flex flex-col md:flex-row md:justify-between space-y-4 md:space-y-0">
                    <button className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition">
                        Zmień hasło
                    </button>
                    <button className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition">
                        Zmień nazwę użytkownika
                    </button>
                </div>
            </div>
        </Template>
    );
}