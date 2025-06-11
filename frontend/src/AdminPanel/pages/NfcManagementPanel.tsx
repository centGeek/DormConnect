import { useNavigate } from "react-router-dom";
import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";


export default function NfcManagementPanel() {
    const navigate = useNavigate();

    return (
        <Template buttons={mainPageButtons}>
            <button
                type="button"
                className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition"
                onClick={() => navigate('/admin-panel')}
            >
                ← Powrót
            </button>
            <div>
                <h1 className="text-xl">Panel zarządzania inteligentnymi zamkami</h1>
            </div>
        </Template>
    )
}