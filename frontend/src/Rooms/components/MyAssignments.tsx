import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Template from "../../Template/Template.tsx";
import './MyAssignments.css';

const MyAssignments: React.FC = () => {
    interface AssignmentsDTO {
        id: number;
        userId: number;
        userFullName: string;
        roomNumber: string;
        roomFloor: number;
        startDate: string;
        endDate: string | null;
    }

    const [assignments, setAssignments] = useState<AssignmentsDTO[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [view, setView] = useState<'current' | 'historical'>('current');

    useEffect(() => {
        const fetchAssignments = async () => {
            try {
                const token = document.cookie
                    .split('; ')
                    .find(row => row.startsWith('token='))?.split('=')[1];

                if (!token) {
                    console.error('Token not found in cookie');
                    setError('Token not found in cookie.');
                    return;
                }

                const response = await axios.get<AssignmentsDTO[]>('/api/dorm/assign/myAssigns', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                setAssignments(response.data);
            } catch (err) {
                console.error('Error while fetching assignments:', err);
                setError('An error occurred while loading assignments.');
            }
        };

        fetchAssignments();
    }, []);

    const now = new Date().toISOString();
    const filteredAssignments = assignments
        .filter(a => {
            if (view === 'current') {
                return !a.endDate || a.endDate >= now;
            } else {
                return a.endDate && a.endDate < now;
            }
        })
        .sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime());

    return (
        <Template
            footerContent={<p></p>}
            buttons={[
                { text: 'Chat', link: '/chat' },
                { text: 'Events', link: '/events' },
                { text: 'Rooms', link: '/rooms' },
                { text: 'Assignments', link: '/rooms/assignment' },
                { text: 'Form', link: '/rooms/form' }
            ]}
        >
            <div className="mb-6 flex items-center gap-4">
                <span className={`font-medium ${view === 'current' ? 'text-blue-600' : 'text-gray-500'}`}>Current</span>
                <label className="switch">
                    <input
                        type="checkbox"
                        checked={view === 'historical'}
                        onChange={() => setView(view === 'current' ? 'historical' : 'current')}
                    />
                    <span className="slider"></span>
                </label>
                <span className={`font-medium ${view === 'historical' ? 'text-blue-600' : 'text-gray-500'}`}>Historical</span>
            </div>


            <div>
                {error && <p className="text-red-500">{error}</p>}
                {filteredAssignments.length === 0 && !error && <p>No assignments found.</p>}

                <ul id="assignment-list">
                    {filteredAssignments.map((a) => (
                        <li key={a.id} className="assignment-item">
                            <p>
                                <strong>Room:</strong> {a.roomNumber} (Floor {a.roomFloor})
                            </p>
                            <p>
                                <strong>Period:</strong> {a.startDate} – {a.endDate ? a.endDate : 'ongoing'}
                            </p>
                        </li>
                    ))}
                </ul>
            </div>
        </Template>
    );
};

export default MyAssignments;
