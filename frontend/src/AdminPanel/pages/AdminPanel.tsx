import {useContext, useEffect} from "react";
import {UserContext} from "../../Context/UserContext";
import Template from "../../Template/Template";
import {useNavigate} from "react-router-dom";
import userIcon from "../../assets/AdminPanelIcon/user.png"
import dormIcon from "../../assets/AdminPanelIcon/dorm.png"
import addIcon from "../../assets/AdminPanelIcon/add.png"
import eventIcon from "../../assets/AdminPanelIcon/event.png"
import problemIcon from "../../assets/AdminPanelIcon/problem.png"
import documentationIcon from "../../assets/AdminPanelIcon/documentation.png";
import {buttons} from "../../ReusableComponents/buttons.ts";

export default function AdminPanel() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();

    useEffect(() => {
        console.log("User: ", userContext?.user);
    })

    return (
        <Template
            buttons={buttons}>
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
                        onClick={() => navigate('/register/user')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Stwórz użytkownika</p>
                        <img src={addIcon} alt="Add icon" className="h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => navigate('/events/admin/events')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie wydarzeniami</p>
                        <img src={eventIcon} alt="Event icon" className="h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => navigate('/problems')}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Zarządzanie problemami</p>
                        <img src={problemIcon} alt="Problem icon" className="h-20 mt-4"/>
                    </button>
                    <button
                        onClick={() => window.open("http://localhost:8000/swagger-ui/index.html", "_blank")}
                        className="bg-gray-500 text-white text-xl px-6 py-3 rounded-lg shadow hover:bg-gray-400 transition flex flex-col items-center justify-center"
                    >
                        <p>Dokumentacja techniczna</p>
                        <img src={documentationIcon} alt="Documentation API icon" className="h-20 mt-4"/>
                    </button>
                </div>
            </div>
        </Template>
    );
}