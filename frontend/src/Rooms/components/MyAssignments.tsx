// import React, { useEffect, useState } from 'react';
// import axios from 'axios';
// import Template from "../../Template/Template.tsx";
// import { useNavigate } from "react-router-dom";
//
// const MyAssignments: React.FC = () => {
//     interface AssignmentsDTO {
//         id: number;
//         userId: number;
//         userFullName: string;
//         roomNumber: string;
//         roomFloor: number;
//         startDate: string;
//         endDate: string | null;
//     }
//
//     const navigate = useNavigate();
//     const [assignments, setAssignments] = useState<AssignmentsDTO[]>([]);
//     const [error, setError] = useState<string | null>(null);
//     const [view, setView] = useState<'current' | 'historical'>('current');
//     const [endingAssignmentId, setEndingAssignmentId] = useState<number | null>(null);
//     const [newEndDate, setNewEndDate] = useState<string>('');
//
//     useEffect(() => {
//         const fetchAssignments = async () => {
//             try {
//                 const token = document.cookie
//                     .split('; ')
//                     .find(row => row.startsWith('token='))?.split('=')[1];
//
//                 if (!token) {
//                     navigate("/login");
//                     return;
//                 }
//
//                 const response = await axios.get<AssignmentsDTO[]>('/api/dorm/assign/myAssigns', {
//                     headers: {
//                         Authorization: `Bearer ${token}`,
//                     },
//                 });
//
//                 setAssignments(response.data);
//             } catch (err) {
//                 console.error('Error while fetching assignments:', err);
//                 setError('An error occurred while loading assignments.');
//             }
//         };
//
//         fetchAssignments();
//     }, []);
//
//     const now = new Date();
//     const nowIso = now.toISOString();
//
//     const isCurrentlyOngoing = (a: AssignmentsDTO) => {
//         const start = new Date(a.startDate);
//         const end = a.endDate ? new Date(a.endDate) : null;
//         return start <= now && (!end || end >= now);
//     };
//
//     const filteredAssignments = assignments
//         .filter(a => {
//             if (view === 'current') {
//                 return !a.endDate || a.endDate >= nowIso;
//             } else {
//                 return a.endDate && a.endDate < nowIso;
//             }
//         })
//         .sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime());
//
//     const submitEndEarlier = async (id: number) => {
//         try {
//             const token = document.cookie
//                 .split('; ')
//                 .find(row => row.startsWith('token='))?.split('=')[1];
//
//             await axios.put(`/api/dorm/assign/`+id+`/shorten?newEndDate=${newEndDate}`, null,{
//                 headers: { Authorization: `Bearer ${token}` }
//             });
//
//             window.location.reload();
//
//         } catch (e) {
//             console.error("Failed to end assignment earlier", e);
//             setError("Failed to submit earlier end date.");
//         }
//     };
//
//     return (
//         <Template
//             footerContent={<p></p>}
//             buttons={[
//                 { text: 'Chat', link: '/chat' },
//                 { text: 'Events', link: '/events' },
//                 { text: 'Rooms', link: '/rooms' },
//                 { text: 'Assignments', link: '/rooms/assignment' },
//                 { text: 'Form', link: '/rooms/form' }
//             ]}
//         >
//             <div className="mb-6 flex items-center gap-4">
//     <span className={`font-medium transition-colors ${view === 'current' ? 'text-blue-600' : 'text-gray-400'}`}>
//         Current
//     </span>
//
//                 <button
//                     onClick={() => setView(view === 'current' ? 'historical' : 'current')}
//                     className={`w-14 h-8 flex items-center bg-gray-300 rounded-full p-1 transition-colors duration-300 ${
//                         view === 'historical' ? 'bg-blue-500' : ''
//                     }`}
//                 >
//                     <div
//                         className={`bg-white w-6 h-6 rounded-full shadow-md transform transition-transform duration-300 ${
//                             view === 'historical' ? 'translate-x-6' : ''
//                         }`}
//                     ></div>
//                 </button>
//
//                 <span className={`font-medium transition-colors ${view === 'historical' ? 'text-blue-600' : 'text-gray-400'}`}>
//         Historical
//     </span>
//             </div>
//
//
//             {error && <p className="text-red-500 mb-4">{error}</p>}
//             {filteredAssignments.length === 0 && !error && <p>No assignments found.</p>}
//
//             <ul className="space-y-6">
//                 {filteredAssignments.map((a) => {
//                     const currentlyOngoing = isCurrentlyOngoing(a);
//                     return (
//                         <li key={a.id} className="p-4 border rounded-lg shadow-sm bg-white">
//                             <p><strong>Room:</strong> {a.roomNumber} (Floor {a.roomFloor})</p>
//                             <p><strong>Period:</strong> {a.startDate} – {a.endDate ? a.endDate : 'ongoing'}</p>
//
//                             {currentlyOngoing && (
//                                 <div className="mt-3">
//                                     {endingAssignmentId === a.id ? (
//                                         <div className="flex flex-col sm:flex-row items-start sm:items-center gap-3">
//                                             <input
//                                                 type="date"
//                                                 className="border rounded px-3 py-1"
//                                                 value={newEndDate}
//                                                 min={new Date().toISOString().split('T')[0]}
//                                                 onChange={e => setNewEndDate(e.target.value)}
//                                             />
//                                             <button
//                                                 onClick={() => submitEndEarlier(a.id)}
//                                                 className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded"
//                                             >
//                                                 Confirm
//                                             </button>
//                                             <button
//                                                 onClick={() => setEndingAssignmentId(null)}
//                                                 className="text-gray-500 hover:text-gray-700 text-sm"
//                                             >
//                                                 Cancel
//                                             </button>
//                                         </div>
//                                     ) : (
//                                         <button
//                                             className=" bg-blue-500 hover:bg-blue-600 text-white px-4 py-1 rounded"
//                                             onClick={() => {
//                                                 setEndingAssignmentId(a.id);
//                                                 setNewEndDate('');
//                                             }}
//                                         >
//                                             End earlier
//                                         </button>
//                                     )}
//                                 </div>
//                             )}
//                         </li>
//                     );
//                 })}
//             </ul>
//         </Template>
//     );
// };
//
// export default MyAssignments;