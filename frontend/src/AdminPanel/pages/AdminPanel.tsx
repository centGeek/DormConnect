import { useContext, useEffect } from "react";
import { UserContext } from "../../Context/UserContext";
import Template from "../../Template/Template";
import SettingsItemComponent from "../components/SettingsItemComponent";

export default function AdminPanel() {
    const userContext = useContext(UserContext);

    useEffect(() => {
        console.log("User: " , userContext?.user);
    })

  return (
<Template
            buttons={[
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' },
                { text: 'Common Rooms', link: '/common-rooms' },
                { text: 'Rooms', link: '/rooms' },
                { text: 'Problems', link: '/problems' }]}>

    <div className="flex flex-row items-center">
        <SettingsItemComponent text="Zarządzanie użytkownikami" url="/users/manage"></SettingsItemComponent>
        <SettingsItemComponent text="Zarządzanie domem studenckim" url="/dormitory"></SettingsItemComponent>
        <SettingsItemComponent text="Zarządzanie kontrolą dostępu" url="/nfc/manage"></SettingsItemComponent>
    </div>

</Template>
  );
}