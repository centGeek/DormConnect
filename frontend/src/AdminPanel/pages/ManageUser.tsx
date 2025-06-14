import { useNavigate, useParams } from "react-router-dom";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";
import { UserDTO } from "../interfaces/UserDTO";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { UserContext } from "../../Context/UserContext";
import DeleteUserDialog from "../components/DeleteUserDialog";
import GenerateUUIDDialog from "../components/GenerateUUIDDialog";
import ChangeRoleDialog from "../components/ChangeRoleDialog";
import ChangeEmailDialog from "../components/ChangeEmailDialog";
import LockAccountDialog from "../components/LockAccountDialog";



export default function ManageUser() {
    const navigate = useNavigate();
    const userContext = useContext(UserContext);
    const params = useParams();
    const [currUser, setCurrUser] = useState<UserDTO>();
    const [deleteUserDialogOpen, setDeleteUserDialogOpen] = useState(false);
    const [changeRoleDialogOpen, setChangeRoleDialogOpen] = useState(false);
    const [blockAccountDialogOpen, setBlockAccountDialogOpen] = useState(false);
    const [unblockAccountDialogOpen, setUnblockAccountDialogOpen] = useState(false);
    const [generateUuidDialogOpen, setGenerateUuidDialogOpen] = useState(false);
    const [changeEmailDialogOpen, setChangeEmailDialogOpen] = useState(false);

    const handleDialogClose = () => {
        setDeleteUserDialogOpen(false);
        setChangeRoleDialogOpen(false);
        setBlockAccountDialogOpen(false);
        setUnblockAccountDialogOpen(false);
        setGenerateUuidDialogOpen(false);
        setChangeEmailDialogOpen(false);
    }

    const handleDeleteUserDialog = () => {
        setDeleteUserDialogOpen(!deleteUserDialogOpen);
    }


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
        <>
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
                    <div>
                        <h2 className="text-lg">ID: {currUser?.id}</h2>
                        <h2 className="text-lg">UUID: {currUser?.uuid}</h2>
                        <h2 className="text-lg">Nazwa użytkownika: {currUser?.userName}</h2>
                        <h2 className="text-lg">Email: {currUser?.email}</h2>
                        <h2 className="text-lg">Rola: {currUser?.role}</h2>
                        {currUser?.isActive &&
                            <h2 className="text-lg">Status konta: aktywne</h2>}
                        {!currUser?.isActive &&
                            <h2 className="text-lg">Status konta: nieaktywne</h2>}
                        <DeleteUserDialog />
                        <ChangeRoleDialog/>
                        <LockAccountDialog isLocked={currUser?.isActive}/>
                        <GenerateUUIDDialog/>
                        <ChangeEmailDialog user={currUser}/>
                        <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition">Zarządzaj kartą dostępu</button>
                    </div>

                </div>


            </Template>

        </>

    )
}