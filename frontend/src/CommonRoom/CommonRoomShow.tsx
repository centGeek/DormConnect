import {useState} from "react";
import Template from "../Template/Template.tsx";


function CommonRoomShow(){

    const [floor, setFloor] = useState<number>(0);
    const [roomType, setRoomType] = useState<string>("");

    return(
        <Template>
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
            <div className="common-room-show-container">
                <form>
                    <input type="number" value={floor} onChange={(e) => setFloor(parseInt(e.target.value))} placeholder="Floor" />
                    <input type="radio" value={roomType} onChange={(e) => setRoomType(e.target.value)} placeholder="Room Type" />
                </form>
            </div>
        </Template>
)

}


export default CommonRoomShow;