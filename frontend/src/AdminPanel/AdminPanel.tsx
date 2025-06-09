import { useContext } from "react";
import { UserContext } from "../Context/UserContext";
import Template from "../Template/Template";

export default function AdminPanel() {
    const userContext = useContext(UserContext);

    useEffect(() => {
        console.log("User: " , userContext?.user);
    })

  return (
<Template>
    <div>
      <h1>Admin Panel</h1>
      <p>This is the admin panel where you can manage the application.</p>
    </div>
</Template>
  );
}