import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useContext, useState } from 'react'
import { UserContext } from '../../Context/UserContext';
import UpdateUserDTO from '../interfaces/UpdateUserDTO';
import axios from 'axios';

export default function DeleteUserDialog({ user, onSuccess, onError }: { user: UpdateUserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  const [isOpen, setIsOpen] = useState(false);
  const [role, setRole] = useState(user.role);
  const userContext = useContext(UserContext);


  const handleChangeRole = async () => {
    setIsOpen(false);
    const token = userContext?.token;
    const updatedUserDTO: UpdateUserDTO = {
      uuid: user.uuid,
      userName: user.userName,
      email: user.email,
      role: role,
      isActive: user.isActive
    }

    try {
      console.log("aktualnie wybrana rola: ", role)
      const response = await axios.put('/api/users/update',
        updatedUserDTO,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      if (response.status === 200) {
        if (onSuccess) {
          onSuccess("Rola użytkownika została zmieniona pomyślnie");
          console.log("User role changed successfully:", response.data);
        }
      }
    } catch(error) {
      
      console.error("An error occurred while changing user role: ", error);
      if (onError) {
        onError("Wystąpił błąd podczas zmiany roli użytkownika.");
      }
    }



  }
  return (
    <>
      <button className='w-40  m-2  px-5 py-2 rounded-lg  transition bg-gray-600 text-white hover:bg-gray-500' onClick={() => setIsOpen(true)}>Zmień rolę</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
            <DialogTitle className="font-bold text-2xl">Zmiana roli użytkownika</DialogTitle>
            <Description>
              <label className="block mb-2 text-sm font-medium text-gray-700">Wybierz nową rolę:</label>
              <select value={role} onChange={(e) => setRole(e.target.value)} className="block w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="ADMIN">Administrator</option>
                <option value="STUDENT">Student</option>
                <option value="MANAGER">Menedżer</option>
              </select>
            </Description>
            <div className="flex gap-4">
              <button className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleChangeRole}>Zmień</button>
              <button className="bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}