import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../Context/UserContext";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import UserDTO from "../interfaces/UserDTO";
import { errorResponsePlugin } from "http-proxy-middleware";

export default function UserManagementPanel() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();
    const [users, setUsers] = useState<UserDTO[]>([]);

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

    useEffect(() => {
        fetchUsers();
        console.log("Users:", users)
    }, []);

    return (
        <Template buttons={mainPageButtons}>
            <div>
                <button
                    type="button"
                    className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                    onClick={() => navigate('/admin-panel')}
                >
                    ← Powrót
                </button>
                <div>
                    <h1 className="text-xl">Panel zarządzania użytkownikami</h1>
                    <form>
                        <label htmlFor="username-search" className="block mb-2">Wyszukaj po nazwie użytkownika lub adresie e-mail</label>
                        <input type="text" placeholder="Nazwa użytkownika/email" name="username-search" className="border-2"></input>
                    </form>
                    <p>Wszyscy użytkownicy:</p>

                    {users.map((user) => (
                        <div key={user.id} className="border-2 p-2 my-2">
                            <p><strong>ID:</strong> {user.id}</p>
                            <p><strong>Nazwa użytkownika:</strong> {user.userName}</p>
                            <p><strong>Email:</strong> {user.email}</p>
                            <p><strong>Rola:</strong> {user.role}</p>
                            <p><strong>Konto aktywne: </strong> {String(user.isActive)} </p>
                            <button className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition" onClick={() => navigate("/users/manage/" + user.id)}>Zarządzaj</button>
                        </div>)
                    )}
                </div>
            </div>
        </Template>
    )
}