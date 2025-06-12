import Template from "../Template/Template";

export default function AccountSettingsPanel() {
    return (
        <Template
                    buttons={[
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' },
                { text: 'Common Rooms', link: '/common-rooms' },
                { text: 'Rooms', link: '/rooms' },
                { text: 'Problems', link: '/problems' }]}>
        <div>
                <div id="container">
                    <h1 className="text-xl">Panel Zarządzania użytkownikami</h1>
                </div>


        </div>

        </Template>
    )
}