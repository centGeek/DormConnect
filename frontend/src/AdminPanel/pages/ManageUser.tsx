import { useNavigate, useParams } from "react-router-dom";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";
import { UserDTO } from "../interfaces/UserDTO";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { UserContext } from "../../Context/UserContext";


export default function ManageUser() {
    const navigate = useNavigate();
    const userContext = useContext(UserContext);
    const params = useParams();
    const [currUser, setCurrUser] = useState<UserDTO>();

    const fetchUser = async () => {
        try {
            console.log("token: ", userContext?.token);
            console.log("params: ", params);
            const response = await axios.get(
                `/api/users/get/${params.id}`,
                {
                    headers: {
                        "Authorization": `Bearer ${userContext?.token}`,
                      }
                }
            )
            if (response.status === 200) {
                setCurrUser(response.data);
                console.log("User fetched successfully:", response.data);
            } else {
                console.error("Failed to fetch user: ", response.status);
                //navigate('/users/manage');
            }

        } catch (error) {
            console.error("Error fetching user: ", error);
            navigate('/users/manage');
        }
    };

    useEffect(() => {
        fetchUser();
    }, []);
    return (
        


        <Template
        buttons={mainPageButtons}>
            <button
                type="button"
                className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                onClick={() => navigate('/users/manage')}
            >
                ← Powrót
            </button>
            <div>
                <h1 className="text-xl">Zarządzanie użytkownikiem</h1>
            </div>
        </Template>
    )
}