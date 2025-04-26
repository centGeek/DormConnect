import React, { useState } from "react";
import axios from "axios";

// <- dzięki temu axios zawsze wysyła ciasteczka
axios.defaults.withCredentials = true;

const Room: React.FC = () => {
    // --- login state ---
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loggedIn, setLoggedIn] = useState(false);

    // --- room form state ---
    const [number, setNumber] = useState("");
    const [capacity, setCapacity] = useState(1);
    const [floor, setFloor] = useState(1);

    // --- login handler ---
    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await axios.post(
                "/login",
                new URLSearchParams({
                    email,
                    password
                }),
                {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                }
            );
            setLoggedIn(true);
            alert("Logged in successfully!");
        } catch (error) {
            console.error(error);
            alert("Login failed!");
        }
    };

    // --- add room handler ---
    const handleAddRoom = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await axios.post("/api/dorm/room", {
                number,
                capacity,
                floor,
                active: true,
                roomAssigns: [],
                groupedRooms: null
            });
            alert("Room created successfully!");
            // Optionally clear form
            setNumber("");
            setCapacity(1);
            setFloor(1);
        } catch (error) {
            console.error(error);
            alert("Failed to create room.");
        }
    };

    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold mb-6">Rooms Page</h1>

            {!loggedIn && (
                <form onSubmit={handleLogin} className="flex flex-col gap-4 mb-8 border p-4 rounded-lg">
                    <h2 className="text-xl font-semibold">Login</h2>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="border p-2 rounded"
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="border p-2 rounded"
                        required
                    />
                    <button
                        type="submit"
                        className="bg-green-500 text-white p-2 rounded hover:bg-green-600 transition"
                    >
                        Login
                    </button>
                </form>
            )}

            {loggedIn && (
                <form onSubmit={handleAddRoom} className="flex flex-col gap-4 border p-4 rounded-lg">
                    <h2 className="text-xl font-semibold">Add New Room</h2>
                    <input
                        type="text"
                        placeholder="Room Number"
                        value={number}
                        onChange={(e) => setNumber(e.target.value)}
                        className="border p-2 rounded"
                        required
                    />
                    <input
                        type="number"
                        placeholder="Capacity"
                        value={capacity}
                        onChange={(e) => setCapacity(parseInt(e.target.value))}
                        min={1}
                        className="border p-2 rounded"
                        required
                    />
                    <input
                        type="number"
                        placeholder="Floor"
                        value={floor}
                        onChange={(e) => setFloor(parseInt(e.target.value))}
                        className="border p-2 rounded"
                        required
                    />
                    <button
                        type="submit"
                        className="bg-blue-500 text-white p-2 rounded hover:bg-blue-600 transition"
                    >
                        Add Room
                    </button>
                </form>
            )}
        </div>
    );
};

export default Room;
