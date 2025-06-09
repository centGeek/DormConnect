import Template from "../../Template/Template";
import { mainPageButtons } from "../interfaces/MainPageButtons";


export default function NfcManagementPanel() {
    return (
       <Template buttons={mainPageButtons}>
       <div>
            <h1 className="text-xl">Panel zarządzania inteligentnymi zamkami</h1>
       </div>
       </Template>
    )
}