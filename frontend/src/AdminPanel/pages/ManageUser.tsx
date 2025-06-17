import { useNavigate, useParams } from "react-router-dom";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";
import { UserDTO } from "../interfaces/UserDTO";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { UserContext } from "../../Context/UserContext";
import DeleteUserDialog from "../components/DeleteUserDialog";
import GenerateUUIDDialog from "../components/ProgramCardDialog";
import ChangeRoleDialog from "../components/ChangeRoleDialog";
import ChangeEmailDialog from "../components/ChangeEmailDialog";
import LockAccountDialog from "../components/LockAccountDialog";
import ErrorDialog from "../components/ErrorDialog";
import SuccessDialog from "../components/SuccessDialog";
import { buttons } from "../../ReusableComponents/buttons.ts";
import ProgramCardDialog from "../components/ProgramCardDialog";



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
    const [errorDialogOpen, setErrorDialogOpen] = useState(false);
    const [errorDialogMsg, setErrorDialogMsg] = useState("");
    const [successDialogOpen, setSuccessDialogOpen] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [dialogUrl, setDialogUrl] = useState("");

    const handleDialogChangeSuccess = (message: string) => {
        setSuccessMessage(message);
        setSuccessDialogOpen(true);
        setDialogUrl('/users/manage');
    }
    const handleDialogChangeError = (msg: string) => {
        setErrorDialogMsg(msg);
        setErrorDialogOpen(true);
    };


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
                buttons={buttons}>
                <button
                    type="button"
                    className="bg-gray-600 text-white hover:bg-gray-500 px-5 py-2 rounded-lg  transition"
                    onClick={() => navigate('/users/manage')}
                >
                    ← Powrót
                </button>
                <div className="max-w-7xl  mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
                    <h1 className="text-2xl font-bold mb-6 text-center">Zarządzanie użytkownikiem</h1>
                    <div className="w-4xl mx-auto mt-10 p-6 bg-gray-100 hadow-md rounded-lg">
                        <div className=" flex h-10 p-5 flex-col md:flex-row hover:bg-gray-200 rounded-lg md:items-center md:justify-between">
                            <label className="text-xl text-black">ID: </label>
                            <label className="text-xl text-black">{currUser?.id} </label>
                        </div>

                        <div className=" h-10 p-5 mt-2 flex flex-col md:flex-row hover:bg-gray-200 rounded-lg md:items-center md:justify-between">
                            <label className="text-xl text-black">Nazwa użytkownika: </label>
                            <label className="text-xl text-black">{currUser?.userName} </label>
                        </div>

                        <div className="h-10 p-5 mt-2 flex flex-col md:flex-row hover:bg-gray-200 rounded-lg md:items-center md:justify-between">
                            <label className="text-xl text-black">Email </label>
                            <label className="text-xl text-black">{currUser?.email} </label>
                        </div>

                        <div className="h-10 p-5 mt-2 flex flex-col md:flex-row hover:bg-gray-200 rounded-lg md:items-center md:justify-between">
                            <label className="text-xl text-black">Rola: </label>
                            <label className="text-xl text-black">{currUser?.role} </label>
                        </div>


                        {currUser?.isActive && <div className="h-10 p-5 mt-2 flex flex-col md:flex-row hover:bg-gray-200 rounded-lg md:items-center md:justify-between">
                            <label className="text-xl text-black">Status konta: </label>
                            <label className="text-xl text-black">aktywne </label>
                        </div>}

                        {!currUser?.isActive && <div className="h-10 p-5 mt-2 flex flex-col md:flex-row hover:bg-gray-200 rounded-lg md:items-center md:justify-between">
                            <label className="text-xl text-black">Status konta: </label>
                            <label className="text-xl text-black">nieaktywne </label>
                        </div>}

                        <div className="mt-8 flex flex-col md:flex-row md:justify-between space-y-4 md:space-y-0">

                            {currUser && currUser.role != 'ADMIN' && <DeleteUserDialog
                                user={currUser}
                                onError={handleDialogChangeError}
                                onSuccess={handleDialogChangeSuccess} />}

                            {currUser && <ChangeRoleDialog user={currUser} onError={handleDialogChangeError} onSuccess={handleDialogChangeSuccess} />}

                            {currUser && <LockAccountDialog
                                user={currUser}
                                onError={handleDialogChangeError}
                                onSuccess={handleDialogChangeSuccess} />}

                            {currUser && <ProgramCardDialog
                                user={currUser}
                                onError={handleDialogChangeError}
                                onSuccess={handleDialogChangeSuccess} />}

                            {currUser && <ChangeEmailDialog user={currUser} onError={handleDialogChangeError} onSuccess={handleDialogChangeSuccess} />}



                        </div>
                    </div>

                </div>


            </Template>
            <ErrorDialog
                open={errorDialogOpen}
                onClose={() => setErrorDialogOpen(false)}
                message={errorDialogMsg}
            />
            <SuccessDialog
                open={successDialogOpen}
                onClose={() => setSuccessDialogOpen(false)}
                message={successMessage}
            />
        </>

    )
}