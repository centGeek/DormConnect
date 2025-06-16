import Template from "../Template/Template";
import {buttons} from "../ReusableComponents/buttons.ts";

export default function AccountSettingsPanel() {
    return (
        <Template
                    buttons={buttons}>
        <div>
                <div id="container">
                    <h1 className="text-xl">Panel Zarządzania użytkownikami</h1>
                </div>


        </div>

        </Template>
    )
}