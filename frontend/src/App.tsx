import { useContext, useEffect } from 'react';
import { Route, Routes, Navigate } from 'react-router-dom';
import Login from './Login/Login.tsx';
import RegistrationStudent from './Registration/Registration-student.tsx';
import RegistrationManager from './Registration/Registration-manager.tsx';
import Chat from './Chat/Chat.tsx';
import Home from './Home/Home.tsx';
import EventsCreate from './Events/EventsCreate.tsx';
import Events from './Events/Events.tsx';
import { UserContext } from './Context/UserContext.tsx';
import Rooms from './Rooms/RoomPage.tsx';
import AdminEvents from './Events/AdminEvents.tsx';
import EventsEdit from './Events/EventsEdit.tsx';
import DormFormPage from "./Rooms/DormFormPage.tsx";
import CommonRoomShow from "./CommonRoom/CommonRoomShow.tsx";
import CommonRoomSchedule from "./CommonRoom/CommonRoomSchedule.tsx";
import DormProblem from './DormProblems/DormProblems.tsx';
import DormProblemCreate from './DormProblems/CreateProblem.tsx';
import DormProblemManage from './DormProblems/ManageProblem.tsx';
import DormProblemView from './DormProblems/ViewProblem.tsx';
import MyAssignments from "./Rooms/components/MyAssignments.tsx";
import { TemperatureProvider } from './Context/TemperatureContext.tsx';

function App() {
    const userContext = useContext(UserContext);

    useEffect(() => {
        console.log('User context:', userContext?.user?.roles);
    }, [userContext]);

    return (
            <TemperatureProvider>
                <Routes>
                    <Route path="/" element={<Login />} />
                    <Route path="/register/student" element={<RegistrationStudent/>} />
                    <Route path="/register/manager" element={<RegistrationManager/>} />
                    {userContext?.token ? (
                        <>
                            <Route path="/home" element={<Home />} />
                            <Route path="/chat" element={<Chat />} />
                            <Route path="/events" element={<Events />} />
                            <Route path="/events/create" element={<EventsCreate />} />
                            <Route path="/rooms" element={<Rooms />} />
                            <Route path="/rooms/form" element={<DormFormPage />} />
                            <Route path="/events/edit/:eventId" element={<EventsEdit />} />
                            <Route path="/common-rooms" element={<CommonRoomShow/>}/>
                            <Route path="/common-room/:id" element={<CommonRoomSchedule />} />
                            <Route path='/problems' element={<DormProblem/>}/>
                            <Route path="/rooms/assignment" element={<MyAssignments />} />
                        <Route path='/problems/create' element={<DormProblemCreate/>}/>
                        <Route path='/problems/manage/:problemId' element={<DormProblemManage/>}/>
                        <Route path='/problems/details/:problemId' element={<DormProblemView/>}/>
                        {userContext?.user?.roles.some(role => ['ADMIN', 'MANAGER'].includes(role)) && (
                                <Route path="/events/admin/AdminEvents" element={<AdminEvents />} />
                            )}
                        </>
                    ) : (
                        <Route path="*" element={<Navigate to="/" replace />} />
                    )}
                </Routes>
            </TemperatureProvider>

    );
}

export default App;