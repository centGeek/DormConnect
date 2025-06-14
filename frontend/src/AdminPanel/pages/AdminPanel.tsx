import {useContext, useEffect} from "react";
import {UserContext} from "../../Context/UserContext";
import Template from "../../Template/Template";
import {useNavigate} from "react-router-dom";
import {mainPageButtons} from "../interfaces/MainPageButtons.tsx"
import userIcon from "../../assets/AdminPanelIcon/user.png"
import dormIcon from "../../assets/AdminPanelIcon/dorm.png"
import lockIcon from "../../assets/AdminPanelIcon/lock.png"
import eventIcon from "../../assets/AdminPanelIcon/event.png"
import problemIcon from "../../assets/AdminPanelIcon/problem.png"

export default function AdminPanel() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();

    useEffect(() => {
        console.log("User: ", userContext?.user);
    })

    return (
        <Template
            buttons={mainPageButtons}>
            <div className="flex flex-col items-center justify-center space-y-6">
                <h1 className="text-center text-gray-600 text-3xl font-bold">
                    Witaj w panelu administratora!
                </h1>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <button
                        onClick={() => navigate('/users/manage')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie użytkownikami</p>
                        <img src={userIcon} alt="User icon" className="w-20 h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => navigate('/dormitory')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie domem studenckim</p>
                        <img src={dormIcon} alt="Dorm icon" className="h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => navigate('/nfc/manage')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie kontrolą dostępu</p>
                        <img src={lockIcon} alt="Lock icon" className="h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => navigate('/events/admin/events')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie wydarzeniami</p>
                        <img src={eventIcon} alt="Event icon" className="h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => navigate('#')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie problemami</p>
                        <img src={problemIcon} alt="Problem icon" className="h-20 mt-4"/>
                    </button>
                </div>
            </div>
        </Template>
    );
}