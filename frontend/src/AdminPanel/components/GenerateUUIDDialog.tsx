import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useContext, useEffect, useState } from 'react'
import { UserContext } from '../../Context/UserContext'
import UpdateUserDTO from '../interfaces/UpdateUserDTO';
import axios from 'axios';
import { NfcProgrammerDTO } from '../interfaces/NfcProgrammerDTO';

export default function DeleteUserDialog({ user, onSuccess, onError }: { user: UpdateUserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  const [isOpen, setIsOpen] = useState(false);
  const userContext = useContext(UserContext);
  const [nfcProgrammers, setNfcProgrammers] = useState<NfcProgrammerDTO[]>([]);


  const handleFetchAvaliableProgrammers = async () => {
    const token = userContext?.token;

    try {
      const response = await axios.get("/api/nfc-programmer/get-all",
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      if (response.status === 200) {
        setNfcProgrammers(response.data);
        console.log(nfcProgrammers);
        console.log("Available NFC programmers fetched successfully:", response.data);

      }

    } catch (error) {
      console.error("An error occurred while fetching available programmers: ", error);

    }
  }

  const handleProgramCard = async () => {
    const token = userContext?.token;

  }

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
useEffect(() => {
    handleFetchAvaliableProgrammers();
  
})


  return (
    <>
      <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={() => setIsOpen(true)}>Generuj nowe UUID</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Programowanie karty dostępu</DialogTitle>
            <Description>
              <label className='text-gray-600'>Wybierz programator NFC, aby zaprogramować nową kartę dostępu dla użytkownika.</label>
              {Array.isArray(nfcProgrammers) && nfcProgrammers.length !== 0 && <select className='w-full p-2 border rounded-lg' onChange={(e) => console.log(e.target.value)}>
                <option value="" disabled selected>Wybierz programator NFC</option>
                {nfcProgrammers.map((programmer) => (
                  <option key={programmer.uuid} value={programmer.uuid}>
                    {programmer.ipAddress} - {programmer.deviceStatus}
                  </option>
                ))}
              </select>}
                 <button className='bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition' >Sprawdź połączenie</button>

            </Description>
            <div className="flex gap-4">
              <button className='bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition' onClick={handleGenerateUUID}>Programuj</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}