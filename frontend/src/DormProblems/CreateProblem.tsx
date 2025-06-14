import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import Template from '../Template/Template';
import { UserContext } from '../Context/UserContext';





function DormProblemCreate() {
    const navigate = useNavigate();
    const [problemDesc, setProblemDesc] = useState('');
    const [problemDate, setProblemDate] = useState('');
    const [problemName, setProblemName] = useState('');
    const userContext = useContext(UserContext);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const createdDormProblem = {
            name: problemName,
            description: problemDesc,
            problemDate: problemDate,
        }

        console.log(createdDormProblem);
        const response = await fetch('/api/dorm-problem/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${userContext?.token}`
            },
            body: JSON.stringify(createdDormProblem),
            credentials: 'include'
        });


        console.log("Record added successfully");
        console.log(response.status);
        navigate('/problems')
    };



    useEffect(() => {
    }, [])

    return (
        <Template buttons={[
            {text: 'Chat', link: '/chat'},
            {text: 'Wydarzenia', link: '/events'},
            {text: 'Pokoje wspólne', link: '/common-rooms'},
            {text: 'Pokój', link: '/rooms/myInfo'},
            {text: 'Zgłoś problem', link: '/problems'}
        ]}>
    <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <div className="w-full md:w-1/4 flex justify-center items-start p-5">
            <button
                type="button"
                className="bg-gray-600 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition w-full md:w-auto"
                onClick={() => navigate(-1)}
            >
                ← Powrót
            </button>
        </div>

        <h2 className="text-3xl font-bold text-gray-800 mb-8 text-center">Zgłoś Problem</h2>

        <form className="space-y-6" name="dorm-problem-form" onSubmit={handleSubmit}>
            <div className='space-y-2'>
                <label className="block text-gray-700 font-medium">Nazwa problemu:</label>
                <input
                    type='text'
                    name='name'
                    value={problemName}
                    onChange={(e) => setProblemName(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500"
                    required
                />
            </div>

            <div className="space-y-2">
                <label className="block text-gray-700 font-medium">Data wystąpienia problemu:</label>
                <input
                    type="date"
                    name="problemDate"
                    value={problemDate}
                    max={new Date().toISOString().split('T')[0]}
                    onChange={(e) => setProblemDate(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500"
                    required
                />
            </div>

            <div className="space-y-2">
                <label className="block text-gray-700 font-medium">Opis:</label>
                <textarea
                    name="problemDesc"
                    value={problemDesc}
                    placeholder="Opisz zdarzenie"
                    onChange={(e) => setProblemDesc(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-gray-500 min-h-[120px]"
                    required
                />
            </div>

            <button
                type="submit"
                className="w-full bg-gray-800 text-white font-bold py-3 px-4 rounded-lg hover:bg-gray-700 transition duration-300"
            >
                Zgłoś problem
            </button>
        </form>
    </div>
</Template>
    )
}

export default DormProblemCreate;