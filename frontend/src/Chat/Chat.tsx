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
    const [isLoading, setIsLoading] = useState(false);

    const handleSend = async () => {
        if (input.trim()) {
            const userMessage: Message = { text: input, sender: 'user' };
            setMessages(prev => [...prev, userMessage]);
            setInput('');
            setIsLoading(true);

            try {
                const response = await axios.get('http://localhost:8091/chat/get-message', { params: { message: input } });
                const llmMessage: Message = { text: response.data.reply, sender: 'llm' };
                setMessages(prev => [...prev, llmMessage]);
            } catch (error) {
                console.error('Error fetching response from backend:', error);
                const errorMessage: Message = { text: 'Error during communication', sender: 'llm' };
                setMessages(prev => [...prev, errorMessage]);
            } finally {
                setIsLoading(false);
            }
        }
    };

    return (
        <Template
            buttons={[
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' },
                { text: 'Common Rooms', link: '/common-rooms' },
                { text: 'Rooms', link: '/rooms' },
                { text: 'Problems', link: '/problems' }]}
        >
            <div className="chat-container flex flex-col flex-grow w-full h-full max-w-screen-lg mx-auto px-4 py-6 bg-gray-100 rounded-lg shadow-md">
                <h1 className="text-center text-4xl font-extrabold text-gray-800 mb-4">Welcome to Our Chat</h1>
                <h3 className="text-center text-lg text-gray-600">Feel free to ask any questions about studies at Lodz University of Technology</h3>
                <hr className="border-gray-300 my-4" />
                <div className="chat-window flex-grow overflow-y-auto bg-white p-4 rounded-lg shadow-inner">
                    {messages.length === 0 ? (
                        <p className="placeholder text-gray-500 text-center">No messages yet</p>
                    ) : (
                        messages.map((msg, index) => (
                            <div key={index} className={`chat-message ${msg.sender} mb-2`}>
                                {msg.text}
                            </div>
                        ))
                    )}
                    {isLoading && (
                        <div className="chat-message llm mb-2">
                            <div className="loading-dots">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                        </div>
                    )}
                </div>
                <div className="chat-input-area mt-4 flex items-center">
                    <input
                        type="text"
                        placeholder="Ask wanderer"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                        className="flex-grow px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                    />
                    <button
                        onClick={handleSend}
                        className="ml-4 px-4 py-2 bg-gray-500 text-white font-bold rounded-lg hover:bg-white hover:text-gray-500 border transition"
                    >
                        Send message
                    </button>
                </div>
            </div>
        </Template>
    );
}

export default ChatPage;