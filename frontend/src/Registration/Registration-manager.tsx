import React, {useState, useContext} from 'react';
import Template from "../Template/Template.tsx";
import {useNavigate} from "react-router-dom";
import {UserContext} from "../Context/UserContext.tsx";
import {buttons} from "../ReusableComponents/buttons.ts";
import Cookies from "js-cookie";

function RegistrationManager() {
    const [userName, setUserName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [role, setRole] = useState<{ value: string; label: string }>({value: 'MANAGER', label: 'Manager'});
    const [name, setName] = useState<string>('');
    const [surname, setSurname] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [success, setSuccess] = useState<boolean>(false);
    const userContext = useContext(UserContext);
    const navigator = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

        if (password !== confirmPassword) {
            setError('Hasła nie są zgodne');
            return;
        }

        if (password.length < 4) {
            setError('Hasło musi mieć co najmniej 4 znaki');
            return;
        }

        try {
            const manager = {
                name,
                surname,
                user: {
                    userName,
                    email,
                    password,
                    isActive: true
                }
            };
            if (role.value === 'STUDENT') {
                const response = await fetch('/register/student', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${Cookies.get('token')}`,
                    },
                    body: JSON.stringify(manager)
                });

                if (!response.ok) {
                    let errorMessage = `Rejestracja nie powiodła się, status: ${response.status}`;
                    try {
                        const errorData = await response.json();
                        if (errorData.message) errorMessage = errorData.message;
                        else if (errorData.error) errorMessage = errorData.error;
                    } catch {
                        errorMessage = `Rejestracja nie powiodła się: ${response.statusText || 'Nieznany błąd'}`;
                    }
                    throw new Error(errorMessage);
                }

                setSuccess(true);
                setUserName('');
                setEmail('');
                setPassword('');
                setConfirmPassword('');
                setName('');
                setSurname('');
            }
            if (role.value === 'MANAGER' && !userContext?.user?.roles.includes('ADMIN')) {
                setError('Tylko administrator może tworzyć managerów');
                return;
            } else {
                const response = await fetch('/register/manager', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${Cookies.get('token')}`,
                    },
                    body: JSON.stringify(manager)
                });

                if (!response.ok) {
                    let errorMessage = `Rejestracja nie powiodła się, status: ${response.status}`;
                    try {
                        const errorData = await response.json();
                        if (errorData.message) errorMessage = errorData.message;
                        else if (errorData.error) errorMessage = errorData.error;
                    } catch {
                        errorMessage = `Rejestracja nie powiodła się: ${response.statusText || 'Nieznany błąd'}`;
                    }
                    throw new Error(errorMessage);
                }

                setSuccess(true);
                setUserName('');
                setEmail('');
                setPassword('');
                setConfirmPassword('');
                setName('');
                setSurname('');
            }


        } catch (error) {
            setError(error instanceof Error ? error.message : 'Rejestracja nie powiodła się. Spróbuj ponownie.');
        }
    };

    return (
        <Template
        buttons = {buttons}>
            <div className="flex flex-col items-center justify-center  bg-gray-100">
                <button
                    type="button"
                    className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition self-start mb-4"
                    onClick={() => navigator(-1)}
                >
                    ← Powrót
                </button>
                <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-md">
                    <h2 className="text-xl font-semibold text-center text-gray-600 mb-4">Tworzenie użytkownika</h2>

                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="name" className="block text-gray-700 font-bold mb-2">Imię</label>
                                <input
                                    type="text"
                                    id="name"
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    required
                                />
                            </div>
                            <div>
                                <label htmlFor="surname" className="block text-gray-700 font-bold mb-2">Nazwisko</label>
                                <input
                                    type="text"
                                    id="surname"
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                                    value={surname}
                                    onChange={(e) => setSurname(e.target.value)}
                                    required
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="userName" className="block text-gray-700 font-bold mb-2">Nazwa
                                użytkownika</label>
                            <input
                                type="text"
                                id="userName"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                                value={userName}
                                onChange={(e) => setUserName(e.target.value)}
                                required
                            />
                        </div>

                        <div>
                            <label htmlFor="email" className="block text-gray-700 font-bold mb-2">Email</label>
                            <input
                                type="email"
                                id="email"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-gray-700 font-bold mb-2">Hasło</label>
                            <input
                                type="password"
                                id="password"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-300"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                minLength={4}
                            />
                            <p className="text-gray-500 text-xs mt-1">Musi mieć co najmniej 4 znaki</p>
                        </div>

                        <div>
                            <label htmlFor="confirmPassword" className="block text-gray-700 font-bold mb-2">Potwierdź
                                hasło</label>
                            <input
                                type="password"
                                id="confirmPassword"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-300"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label className="block text-gray-700 font-bold mb-2">Wybierz rolę</label>
                            <select
                                id="role"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-500"
                                value={role.value}
                                onChange={(e) => setRole({
                                    value: e.target.value,
                                    label: e.target.options[e.target.selectedIndex].text
                                })}
                            >
                                <option value="MANAGER">Manager</option>
                                <option value="STUDENT">Student</option>
                            </select>
                        </div>

                        {error && <p className="text-red-500 text-sm">{error}</p>}
                        {success && (
                            <p className="text-green-500 text-sm">
                                Użytkownik został pomyślnie zarejestrowany!
                            </p>
                        )}

                        <button
                            type="submit"
                            className="w-full bg-gray-500 text-white font-bold py-2 px-4 rounded-lg border hover:bg-white hover:text-gray-500 transition"
                        >
                            Zarejestruj
                        </button>

                    </form>

                </div>
            </div>
        </Template>
    );
}

export default RegistrationManager;