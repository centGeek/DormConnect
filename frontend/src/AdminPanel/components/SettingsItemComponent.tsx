import { useNavigate } from "react-router-dom";

export default function SettingsItemComponent({text, url}: {text: string, url: string}) {

    const navigate = useNavigate();

    const handleNavigate = () => {
        if (url !== "") {
            navigate(url);
        }
    }

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-10 bg-blue-400 text-center self-center justify-center text-white text-xl">
            <button className="hover:bg-blue-600 w-max h-max" onClick={handleNavigate}>{text}</button>
        </div>
    )
}