import { useNavigate } from "react-router-dom";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";
import { UserContext } from "../../Context/UserContext";
import { useContext } from "react";
import axios from "axios";


export default function NfcManagementPanel() {
    const navigate = useNavigate();
    const userContext = useContext(UserContext);

    const fetchNfcDevices = async () => {
        try {
            const response = await axios.get("/api/nfc")
        } catch (error) {
            console.error("Error while fetching NFC devices: ", error)
        }
    }

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
            <button
                type="button"
                className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                onClick={() => navigate('/admin-panel')}
            >
                ← Powrót
            </button>
            <div>
                <h1 className="text-xl">Dostępne programatory kart:</h1>
            </div>
            <div>
                <h1 className="text-xl">Dostępne urządzenia kontroli dostępu:</h1>
            </div>
        </Template>
    )
}