import { useState } from 'react';
import Template from '../Template/Template.tsx';
import './Chat.css';
import axios from 'axios';

interface Message {
    text: string;
    sender: 'user' | 'llm';
}

function ChatPage() {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState('');

    const handleSend = async () => {
        if (input.trim()) {
            const userMessage: Message = { text: input, sender: 'user' };
            setMessages(prev => [...prev, userMessage]);
            setInput('');

            try {
                const response = await axios.get('http://localhost:8091/chat/get-message', {params: {message: input} });
                const llmMessage: Message = { text: response.data.reply, sender: 'llm' };
                setMessages(prev => [...prev, llmMessage]);
            } catch (error) {
                console.error('Error fetching response from backend:', error);
                const errorMessage: Message = { text: 'Error during comunication', sender: 'llm' };
                setMessages(prev => [...prev, errorMessage]);
            }
        }
    };

    return (
        <Template
            buttons={[{ text: 'Chat', link: '/chat' }, { text: 'Events', link: '/events' }]}
            footerContent={<p></p>}
        >
            <div className="chat-container">
                <div className="chat-window">
                    {messages.length === 0 ? (
                        <p className="placeholder">Just ask</p>
                    ) : (
                        messages.map((msg, index) => (
                            <div key={index} className={`chat-message ${msg.sender}`}>{msg.text}</div>
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