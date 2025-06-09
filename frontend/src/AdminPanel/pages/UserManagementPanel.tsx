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
    const[users, setUsers] = useState<UserDTO[]>([]);

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

        } catch(error) {
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
                <h1 className="text-xl">User management panel</h1>
            </div>
        </Template>
    )
}