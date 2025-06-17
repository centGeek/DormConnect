import {useContext, useEffect, useState} from "react";
import {UserContext} from "../../Context/UserContext";
import Template from "../../Template/Template";
import {mainPageButtons} from "../interfaces/MainPageButtons";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import UserDTO from "../interfaces/UserDTO";
import {errorResponsePlugin} from "http-proxy-middleware";
import {buttons} from "../../ReusableComponents/buttons.ts";

export default function UserManagementPanel() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [search, setSearch] = useState("");

    const fetchUsers = async () => {
        try {
            const response = axios.get("/api/users/getAll",
                {
                    headers: {
                        "Authorization": `Bearer ${userContext?.token}`,
                    }
                }
            )
            const userData = await response;
            setUsers(userData.data);
            console.log("Users fetched successfully:", userData.data);

        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };
    const filteredUsers = users.filter(
        user =>
            user.userName.toLowerCase().includes(search.toLowerCase()) ||
            user.email.toLowerCase().includes(search.toLowerCase())
    );

    useEffect(() => {
        fetchUsers();
        console.log("Users:", users)
    }, []);

    return (
        <Template buttons={buttons}>
            <div className="max-w-7xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
                <button
                    type="button"
                    className="bg-gray-600 text-white hover:bg-gray-500 px-5 py-2 rounded-lg transition mb-6"
                    onClick={() => navigate('/admin-panel')}
                >
                    ← Powrót
                </button>
                <h1 className="text-2xl font-bold mb-6 text-center">Panel zarządzania użytkownikami</h1>
                <form className="mb-8 flex flex-col items-center">
                    <label htmlFor="username-search" className="text-xl text-center block mb-2 text-gray-700 font-semibold">
                        Wyszukaj po nazwie użytkownika lub adresie e-mail
                    </label>
                    <input
                        type="text"
                        placeholder="Nazwa użytkownika/email"
                        name="username-search"
                        className="text-center border-2 border-gray-300 rounded-lg px-4 py-2 w-sm focus:outline-none focus:ring-2 focus:ring-gray-400"
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                    />
                </form>
                <p className="mb-4 font-semibold text-gray-700 text-2xl text-center">Wszyscy użytkownicy:</p>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {filteredUsers.map((user) => (
                        <div key={user.id} className="bg-gray-100 shadow hover:bg-gray-200 transition rounded-lg p-6 flex flex-col space-y-2">
                            <div className="flex justify-between">
                                <span className="font-semibold text-gray-700">ID:</span>
                                <span>{user.id}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="font-semibold text-gray-700">Nazwa użytkownika:</span>
                                <span>{user.userName}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="font-semibold text-gray-700">Email:</span>
                                <span>{user.email}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="font-semibold text-gray-700">Rola:</span>
                                <span>{user.role}</span>
                            </div>
                            <div className="flex justify-between">
                                <span className="font-semibold text-gray-700">Konto aktywne:</span>
                                <span>{user.isActive ? "Tak" : "Nie"}</span>
                            </div>
                            <button
                                className="mt-4 bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                                onClick={() => navigate("/users/manage/" + user.id)}
                            >
                                Zarządzaj
                            </button>
                        </div>
                    ))}
                </div>
                {filteredUsers.length === 0 && (
                    <div className="text-center text-gray-500 mt-8">Brak użytkowników spełniających kryteria.</div>
                )}
            </div>
        </Template>
    )
}