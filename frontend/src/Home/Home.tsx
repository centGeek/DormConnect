import { useState } from 'react';
import Template from '../Template/Template.tsx';
import './Home.css';


function ChatPage() {
    const [counter, setCounter] = useState(0);

    return (
        <Template
            buttons={[{text: 'Home', link: '/' }, { text: 'Chat', link: '/chat' }]}
            footerContent={<p></p>}
        >
            <div className="home-container">
                <h1>Welcome in Dorm Connect</h1>
                <h3>It's application designed to manage your dormitory</h3>
                <h4>You can play this counter below</h4>
                <button className="home-button" onClick={() => setCounter(counter + 1)}>{counter}</button>
            </div>

        </Template>
    );
}

export default ChatPage;
