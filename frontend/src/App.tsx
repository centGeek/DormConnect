import { useContext } from 'react';
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
import MyDormRoomsInfo from "./Rooms/components/MyDormRoomsInfo.tsx";
import CommonRoomShow from "./CommonRoom/CommonRoomShow.tsx";
import CommonRoomSchedule from "./CommonRoom/CommonRoomSchedule.tsx";
import DormProblem from './DormProblems/DormProblems.tsx';
import DormProblemCreate from './DormProblems/CreateProblem.tsx';
import DormProblemManage from './DormProblems/ManageProblem.tsx';
import DormProblemView from './DormProblems/ViewProblem.tsx';
import 'leaflet/dist/leaflet.css';
import CreateDormitory from "./DormitoryCreation/CreateDormiotory.tsx";
import RoomDeletion from "./Rooms/components/RoomDeletion.tsx";
import RoomDeleteTest from "./Rooms/RoomDeleteTest.tsx";
import AccountSettingsPanel from './AccountSettingsPanel/AccountSettingsPanel.tsx';
import AdminPanel from './AdminPanel/pages/AdminPanel.tsx';
import UserManagementPanel from './AdminPanel/pages/UserManagementPanel.tsx';
import NfcManagementPanel from './AdminPanel/pages/NfcManagementPanel.tsx';
import ManageUser from './AdminPanel/pages/ManageUser.tsx';


function App() {
    const userContext = useContext(UserContext);

    return (

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
                        <Route path="/rooms/myInfo" element={<MyDormRoomsInfo />} />
                        <Route path="/events/edit/:eventId" element={<EventsEdit />} />
                        <Route path="/common-rooms" element={<CommonRoomShow/>}/>
                        <Route path="/common-room/:id" element={<CommonRoomSchedule />} />
                        <Route path='/problems' element={<DormProblem/>}/>
                        <Route path='/problems/create' element={<DormProblemCreate/>}/>
                        <Route path='/problems/manage/:problemId' element={<DormProblemManage/>}/>
                        <Route path='/problems/details/:problemId' element={<DormProblemView/>}/>
                        {userContext?.user?.roles.some(role => ['ADMIN', 'MANAGER'].includes(role)) && (
                            <Route path="/events/admin/events" element={<AdminEvents />} />
                        )}
                        {userContext?.user?.roles.includes('ADMIN') && (
                            <Route path="/dormitory" element={<CreateDormitory />} />
                        )}
                        {userContext?.user?.roles.includes('ADMIN') && (
                            <Route path="/dormitory/delete" element={<RoomDeletion roomId={4} />} />
                        )}
                        <Route path="/account-settings" element={<AccountSettingsPanel/>}/>
                        <Route path="/admin-panel" element={<AdminPanel/>}/>
                        <Route path="/users/manage" element={<UserManagementPanel/>}/>
                        <Route path="/nfc/manage" element={<NfcManagementPanel/>}/>
                        <Route path="/users/manage/:id" element={<ManageUser/>}/>

                    </>
                ) : (
                    <Route path="*" element={<Navigate to="/" replace />} />
                )}
            </Routes>


    );
}

export default App;