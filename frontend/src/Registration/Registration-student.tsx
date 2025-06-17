import React, { useState } from 'react';

function RegistrationStudent() {
    const [userName, setUserName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [name, setName] = useState<string>('');
    const [surname, setSurname] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [success, setSuccess] = useState<boolean>(false);
    
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');
        
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }
        
        if (password.length < 4) {
            setError('Password must be at least 4 characters long');
            return;
        }
        
        try {
            const student = {
                name,
                surname,
                user: {
                    userName,
                    email,
                    password,
                    isActive: true
                }
            };
            
            const response = await fetch('/register/student', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(student)
            });
            if (response.status === 409) {
                const errorData = await response.json();
                throw new Error(errorData.message);
            }


            if (!response.ok) {
                let errorMessage = `Registration failed with status: ${response.status}`;
                try {
                    const errorData = await response.json();
                    if (errorData.message) errorMessage = errorData.message;
                    else if (errorData.error) errorMessage = errorData.error;
                } catch {
                    errorMessage = `Registration failed: ${response.statusText || 'Unknown error'}`;
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
            
        } catch (error) {
            setError(error instanceof Error ? error.message : 'Rejestracja nie powiodła się');
        }
    };
    
    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="bg-white shadow-lg rounded-lg p-8 w-full max-w-md">
                <h1 className="text-2xl font-bold text-center text-gray-700 mb-6">Witaj Dorm Connect</h1>
                <h2 className="text-xl font-semibold text-center text-gray-600 mb-4">Rejestracja Studenta</h2>
                
                {success ? (
                    <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
                        <p>Rejestracja powiodła się! Teraz możesz się zalogować.</p>
                        <div className="flex justify-center mt-4">
                            <a 
                                href="/" 
                                className="bg-gray-500 text-white font-bold py-2 px-4 rounded-lg hover:bg-white hover:text-gray-500 transition"
                            >
                                Zaloguj się
                            </a>
                        </div>
                    </div>
                ) : (
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
                            <label htmlFor="userName" className="block text-gray-700 font-bold mb-2">Nazwa użytkownika</label>
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
                            <p className="text-gray-500 text-xs mt-1">Hasło musi mieć przynajmniej 4 znaki</p>
                        </div>
                        
                        <div>
                            <label htmlFor="confirmPassword" className="block text-gray-700 font-bold mb-2">Potwierdź hasło</label>
                            <input
                                type="password"
                                id="confirmPassword"
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-gray-300"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                        
                        {error && <p className="text-red-500 text-sm">{error}</p>}
                        
                        <button
                            type="submit"
                            className="w-full bg-gray-500 text-white font-bold py-2 px-4 rounded-lg border hover:bg-white hover:text-gray-500 transition"
                        >
                            Zarejestruj się
                        </button>
                        
                        <div className="text-center mt-4">
                            <p className="text-gray-600">
                                Masz już konto?{" "}
                                <a href="/" className="text-gray-700 font-semibold hover:underline">
                                    Zaloguj się
                                </a>
                            </p>
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}

export default RegistrationStudent;
