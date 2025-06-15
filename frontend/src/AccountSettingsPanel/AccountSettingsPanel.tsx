import Template from "../Template/Template";

export default function AccountSettingsPanel() {
    return (
        <Template
                    buttons={[
                        {text: 'Chat', link: '/chat'},
                        {text: 'Wydarzenia', link: '/events'},
                        {text: 'Pokoje wspólne', link: '/common-rooms'},
                        {text: 'Pokój', link: '/rooms/myInfo'},
                        {text: 'Zgłoś problem', link: '/problems'}]}>
        <div>
                <div id="container">
                    <h1 className="text-xl">Panel Zarządzania użytkownikami</h1>
                </div>


        </div>

        </Template>
    )
}