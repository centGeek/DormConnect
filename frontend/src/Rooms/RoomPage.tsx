import React from 'react';
import FloorCanvas from './components/FloorCanvas';
import Template from "../Template/Template.tsx";

const RoomPage: React.FC = () => {
    return (
        <Template
        footerContent={<p></p>}
        buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' },{text: 'Rooms', link: '/rooms'},{ text: 'Assigmnetns', link: '/rooms/assignment'},{text: 'Form', link: '/rooms/form'}]}>
            <div style={{padding: '20px'}}>
                <h1 style={{fontSize: '2rem', marginBottom: '20px'}}>Manage Floor Rooms</h1>
                <FloorCanvas/>
            </div>
        </Template>
    );
};

export default RoomPage;
