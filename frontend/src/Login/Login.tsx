import React, { useState } from 'react';
import { UserContext } from "../Context/UserContext.tsx";
import { Link } from 'react-router-dom';
import { Mail, Lock } from 'lucide-react';

function Login() {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const userContext = React.useContext(UserContext);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      await userContext?.handleLogin(email, password);
    } catch (error) {
      console.error('Login failed:', error instanceof Error ? error.message : error);
      setError('Nieprawidłowy email lub hasło');
    }
  };

  return (
      <div className="flex items-center justify-center min-h-screen bg-white">
        <div className="bg-white shadow-xl rounded-3xl p-10 w-full max-w-md border border-gray-300">
          <h1
              className="text-3xl font-extrabold text-center text-gray-800 mb-2"
              style={{ fontFamily: "'Poppins', sans-serif" }}>
            Dorm Connect
          </h1>

          <h2 className="text-lg font-medium text-center text-gray-600 mb-6">Login</h2>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="relative">
              <label htmlFor="email" className="sr-only">Email</label>
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
              <input
                  type="email"
                  id="email"
                  placeholder="Email"
                  className="pl-11 w-full py-2.5 pr-4 border border-gray-300 rounded-lg bg-white text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
              />
            </div>
            <div className="relative">
              <label htmlFor="password" className="sr-only">Password</label>
              {error? (<Lock className="absolute left-4 top-1/5  text-gray-400" size={18}/>): (<Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" size={18}/>)}
              <input
                  type="password"
                  id="password"
                  placeholder="Hasło"
                  className="pl-11 w-full py-2.5 pr-4 border border-gray-300 rounded-lg bg-white text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
              />
              {error && <p className="text-red-600 text-sm mt-2 text-center">{error}</p>}
            </div>
            <button
                type="submit"
                className="w-full py-2.5 rounded-lg font-semibold bg-gray-700 text-white hover:bg-gray-800 transition"
            >
              Zaloguj się
            </button>

            <div className="text-center text-sm text-gray-600 space-y-1 pt-2">
              <p>
                Nie masz jeszcze konta?{' '}
                <Link to="/register/student" className="font-semibold text-gray-700 hover:underline">
                  Zarejestruj się jako Student
                </Link>
              </p>
              {/*<p>*/}
              {/*  Looking to manage dorms?{' '}*/}
              {/*  <Link to="/register/manager" className="font-semibold text-gray-700 hover:underline">*/}
              {/*    Register as Manager*/}
              {/*  </Link>*/}
              {/*</p>*/}
            </div>
          </form>
        </div>
      </div>
  );
}

export default Login;