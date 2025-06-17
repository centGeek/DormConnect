import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { use, useContext, useEffect, useState } from 'react'
import { UserContext } from '../../Context/UserContext';
import axios from 'axios';
import UpdateUserDTO from '../interfaces/UpdateUserDTO';
import { set } from 'date-fns';

export default function DeleteUserDialog({ user, onSuccess, onError }: { user: UpdateUserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  const [isOpen, setIsOpen] = useState(false);
  const userContext = useContext(UserContext);
  const [isLocked, setIsLocked] = useState(user.isActive);
  const handleLockAccount = async () => {
    setIsOpen(false);
    const token = userContext?.token;
    const updatedUserDTO: UpdateUserDTO = {
      uuid: user.uuid,
      userName: user.userName,
      email: user.email,
      role: user.role,
      isActive: !user.isActive 
  }
  try {
    const response = await axios.put(
      "/api/users/update", 
      updatedUserDTO,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    )

    if (response.status === 200) {
      if (onSuccess) {
        onSuccess("Status konta użytkownika został pomyślnie zmieniony.");
        console.log("User account status updated successfully:", response.data);
      }
    }
  } catch (error) {
    console.error("An error occurred while locking/unlocking user account: ", error);
    if (onError) {
      onError("Wystąpił błąd podczas zmiany statusu konta użytkownika.");
    }
  }



  }
  return (
    <>

      {!isLocked && 
        <div>
    <button className='w-40 bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={() => setIsOpen(true)}>Odblokuj konto użytkownika</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
            <DialogTitle className="font-bold text-2xl">Odblokuj konto</DialogTitle>
            <Description> Czy na pewno chcesz odblokować konto użytkownika?</Description>
            <div className="flex gap-4">
              <button className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleLockAccount}>Odblokuj</button>
              <button className="bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
        </div>
      }

      {isLocked && 
      <div>
              <button className='w-40  m-2  px-5 py-2 rounded-lg bg-gray-600 text-white hover:bg-gray-500 transition' onClick={() => setIsOpen(true)}>Zablokuj konto użytkownika</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4 ">
          <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
            <DialogTitle className="font-bold text-2xl">Zablokuj konto </DialogTitle>
            <Description>Czy na pewno chcesz zablokować konto użytkownika?</Description>
            <div className="flex gap-4">
              <button className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleLockAccount}>Zablokuj</button>
              <button className=" m-2  px-5 py-2 rounded-lg bg-red-600 text-white hover:bg-red-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </div>}

    </>
  )
}