import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useContext, useState } from 'react'
import { UserContext } from '../../Context/UserContext'
import UpdateUserDTO from '../interfaces/UpdateUserDTO';
import axios from 'axios';

export default function DeleteUserDialog({ user, onSuccess, onError }: { user: UpdateUserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  const [isOpen, setIsOpen] = useState(false);
  const userContext = useContext(UserContext);

  const handleGenerateUUID = async () => {
    setIsOpen(false);
    const token = userContext?.token;
    const updatedUserDTO: UpdateUserDTO = {
      uuid: user.uuid,
      userName: user.userName,
      email: user.email,
      role: user.role,
      isActive: user.isActive
  }
  try {
    const response = await axios.post(
      "/api/users/update-uuid",  
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
        onSuccess("UUID użytkownika zostało pomyślnie zaktualizowane");
        console.log("User UUID updated successfully:", response.data);
      }
    }

  } catch (error) {
    console.error("An error occurred while generating new UUID: ", error);
    if (onError) {
      onError("Wystąpił błąd podczas generowania nowego UUID użytkownika.");
    }
  }

}


  return (
    <>
      <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={() => setIsOpen(true)}>Generuj nowe UUID</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Zmiana UUID użytkownika</DialogTitle>
            <Description>Ta operacja jest nieodwracalna. Czy na pewno chcesz wygenerować nowe UUID?</Description>
            <div className="flex gap-4">
              <button className='bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition' onClick={handleGenerateUUID}>Generuj</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}