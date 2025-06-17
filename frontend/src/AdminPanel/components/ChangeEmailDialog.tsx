import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useContext, useEffect, useState } from 'react'
import { UserContext } from '../../Context/UserContext';
import ErrorAlert from './ErrorAlert';
import { UserDTO } from '../interfaces/UserDTO';
import axios from 'axios';
import UpdateUserDTO from '../interfaces/UpdateUserDTO';
import ErrorDialog from './ErrorDialog';

export default function ChangEmailDialog({ user, onSuccess, onError }: { user: UserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  let [isOpen, setIsOpen] = useState(false);
  const userName = user.userName;
  const [email, setEmail] = useState(user.email);
  const userContext = useContext(UserContext);
  const [error, setError] = useState(false);


  const handleChangeEmail = async () => {
    setIsOpen(false);

    const token = userContext?.token;

    const updatedUserDto: UpdateUserDTO = {
      uuid: user.uuid,
      userName: userName,
      email: email,
      role: user.role,
      isActive: user.isActive
    };

    try {
      const response = await axios.put(
        `/api/users/update`,
        updatedUserDto,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
      if (response.status === 200) {

        if (onSuccess) {
          onSuccess("Adres e-mail został zmieniony pomyślnie");
          console.log("Email changed successfully:", response.data);
        }

      }
    } catch (error) {
      if (onError) {
        onError("Wystąpił błąd podczas zmiany adresu e-mail");
      }
      console.error("An error occured while changin email: ", error);
    }

  };


  return (
    <>
      <button className='w-40 m-2  px-5 py-2 rounded-lg transition bg-gray-600 text-white hover:bg-gray-500' onClick={() => setIsOpen(true)}>Zmień adres <br/>  e-mail</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <form>
          <div className="fixed inset-0 flex w-screen items-center justify-center p-4">

            <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
              <DialogTitle className="font-bold text-2xl">Zmiana adresu e-mail</DialogTitle>
              <Description>
                <label>Wprowadź nowy adres e-mail:</label>
                <input onChange={(e) => setEmail(e.target.value)} type="email" className="block w-full p-2 border border-gray-300 rounded-lg mt-4 focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Wpisz nowy adres e-mail" />

              </Description>
              <div className="flex gap-4">
                <button className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleChangeEmail}>Zmień</button>
                <button className="bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>

              </div>
            </DialogPanel>
          </div>
        </form>
      </Dialog>

    </>
  )
}