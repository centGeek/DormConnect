import { useNavigate } from "react-router-dom";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";
import { UserContext } from "../../Context/UserContext";
import { useContext } from "react";
import axios from "axios";
import {buttons} from "../../ReusableComponents/buttons.ts";


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
        <Template buttons={buttons}>
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