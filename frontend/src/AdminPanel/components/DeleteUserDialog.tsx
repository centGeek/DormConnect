import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useContext, useState } from 'react'
import { UserDTO } from '../interfaces/UserDTO'
import { UserContext } from '../../Context/UserContext';
import axios from 'axios';

export default function DeleteUserDialog({ user, onSuccess, onError }: { user: UserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  let [isOpen, setIsOpen] = useState(false);
  const userName = user.userName;
  const [email, setEmail] = useState(user.email);
  const userContext = useContext(UserContext);
  const [error, setError] = useState(false);

  const handleDeleteUser = async () => {
    setIsOpen(false);
    const token = userContext?.token;
    const userId = user.id;
    try {
      const response = await axios.delete(
        `/api/users/delete/${userId}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      )

      if (response.status === 200) {
        console.log("user deleted successfully: ", response.data);
        if (onSuccess) {
          onSuccess("Użytkownik został pomyślnie usunięty.")
        }
      }

    } catch(error) {
      console.error("Cannot delete user - an error occured: ", error);
      if (onError) {
        onError("Wystąpił błąd podczas usuwania użytkownika.")
      }
    }
  }

  return (
    <>
      <button className='w-40 bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition' onClick={() => setIsOpen(true)}>Usuń użytkownika</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
            <DialogTitle className="font-bold text-2xl">Usuwanie użytkownika</DialogTitle>
            <Description>Ta operacja jest nieodwracalna. Czy na pewno chcesz usunąć użytkownika?</Description>
            <div className="flex gap-4">
              <button className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleDeleteUser}>Usuń</button>
              <button className="bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}