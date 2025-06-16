import React, { useState } from 'react';
import Cookies from "js-cookie";

interface PopUpChangeUsernameProps {
    onClose: () => void;
}

export default function PopUpChangeUsername({ onClose }: PopUpChangeUsernameProps) {
    const [newUsername, setNewUsername] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setErrorMessage(null);

        try {
            const response = await fetch('/api/users/update/username', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + Cookies.get('token'),
                },
                body: JSON.stringify({ newUsername }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Nie udało się zmienić nazwy użytkownika.');
            }

            setIsSuccess(true);
        } catch (error) {
            setErrorMessage(error instanceof Error ? error.message : 'Wystąpił błąd podczas zmiany nazwy użytkownika.');
        }
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-opacity-50 z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full border border-gray-700 max-w-md relative">
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 w-6 h-6 bg-gray-300 rounded-full flex items-center justify-center hover:bg-gray-400 transition"
                >
                    ×
                </button>

                <h2 className="text-xl font-semibold mb-4">Zmień nazwę użytkownika</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label htmlFor="newUsername" className="block text-sm font-medium text-gray-700">Nowa nazwa użytkownika</label>
                        <input
                            type="text"
                            id="newUsername"
                            value={newUsername}
                            onChange={(e) => setNewUsername(e.target.value)}
                            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                            required
                        />
                    </div>
                    {errorMessage && <p className="text-red-500 text-sm mb-4">{errorMessage}</p>}
                    <button
                        type="submit"
                        className="w-full bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700 transition duration-200"
                    >
                        Zmień nazwę użytkownika
                    </button>
                </form>
                {isSuccess && (
                    <div className="mt-4 text-green-600">
                        Nazwa użytkownika została pomyślnie zmieniona!
                    </div>
                )}
            </div>
        </div>
    );
}