import { useState } from 'react';
import Template from '../Template/Template.tsx';
import './Chat.css';

function ChatPage() {
    const [messages, setMessages] = useState<string[]>([]);
    const [input, setInput] = useState('');

    const handleSend = () => {
        if (input.trim()) {
            setMessages(prev => [...prev, input]);
            setInput('');
        }
    };

    return (
        <Template
            buttons={[{text: 'Home', link: '/' }, { text: 'Chat', link: '/chat' }]}
            footerContent={<p></p>}
        >
            <div className="chat-container">
                <div className="chat-window">
                    {messages.length === 0 ? (
                        <p className="placeholder">Just ask</p>
                    ) : (
                        messages.map((msg, index) => (
                            <div key={index} className="chat-message">{msg}</div>
                        ))
                    )}
                </div>
                <div className="chat-input-area">
                    <input
                        type="text"
                        placeholder="Ask wanderer"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                    />
                    <button onClick={handleSend}>Send message</button>
                </div>
            </div>
        </Template>
    );
}

export default ChatPage;
