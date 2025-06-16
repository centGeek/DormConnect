import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useContext, useEffect, useState } from 'react'
import { UserContext } from '../../Context/UserContext'
import UpdateUserDTO from '../interfaces/UpdateUserDTO';
import axios, { HttpStatusCode } from 'axios';
import { NfcProgrammerDTO } from '../interfaces/NfcProgrammerDTO';
import { Nfc } from 'lucide-react';
import { set } from 'date-fns';

export default function ProgramCardDialog({ user, onSuccess, onError }: { user: UpdateUserDTO, onSuccess?: (msg: string) => void, onError?: (msg: string) => void }) {
  const [isOpen, setIsOpen] = useState(false);
  const userContext = useContext(UserContext);
  const [nfcProgrammers, setNfcProgrammers] = useState<NfcProgrammerDTO[]>([]);
  const [selectedProgrammer, setSelectedProgrammer] = useState<NfcProgrammerDTO>();

    const handleCheckConnection = async () => {
    if (!selectedProgrammer) {
      console.error("no nfc programmer selected")
      
    }
    
    try {
      const token = userContext?.token;
      console.log("Selected NFC Programmer: ", selectedProgrammer);
      console.log("nfc programmers:", nfcProgrammers[0]);

      const response = await axios.get("/api/nfc-programmer/check-connection/" + selectedProgrammer?.uuid,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      )
      if (response.status === HttpStatusCode.Ok) {
        console.log("Connection check successful: ")
      } else {
        console.error("Connection check failed: ", response);
      }

    } catch (error) {
      console.error("An error occured while checking connection: ", error )
    }
  }

  const handleChangeSelectedProgrrammer = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedProgrammerUUID = e.target.value;
    console.log("Selected NFC Programmer UUID: ", selectedProgrammerUUID);
    const programmer = nfcProgrammers.find(p => p.uuid === selectedProgrammerUUID);
    if (programmer) {
      setSelectedProgrammer(programmer);
      console.log("Selected NFC Programmer: ", programmer);
    }
  }


    const handleProgramCard = async () => {
    const token = userContext?.token;
    if (!selectedProgrammer) {
      console.error("No NFC programmer selected");
      return;
    }
    try {
      const response = await axios.post("/api/nfc-programmer/program-card",
        {
          deviceUuid: selectedProgrammer.uuid,
          userUuid: user.uuid
        },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      )
      if (response.status === HttpStatusCode.Ok) {
        console.log("Card programmed successfully: ", response.data);
        if (onSuccess) {
          onSuccess("Karta dostępu została zaprogramowana pomyślnie.");
        }
        setIsOpen(false); // Close the dialog on success
      }

    } catch (error) {
      console.error("An error occurred while programming the card: ", error);
      if (onError) {
        onError("An error occurred while programming the card. Please try again.");
        setIsOpen(false); // Close the dialog on error
      }
    }

  }

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

        setSelectedProgrammer(response.data[0]); 

        console.log(nfcProgrammers);

        console.log("selected: " + selectedProgrammer)// Set the first programmer as selected by default
        console.log(nfcProgrammers);
        console.log("Available NFC programmers fetched successfully:", response.data);

      }

    } catch (error) {
      console.error("An error occurred while fetching available programmers: ", error);

    }
}
    const handleSetOpen = () => {
      setIsOpen(true);
      handleFetchAvaliableProgrammers();
    }


  return (
    <>
      <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={handleSetOpen}>Zaprogramuj kartę dostępu</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Programowanie karty dostępu</DialogTitle>
            <Description>
              <label className='text-gray-600'>Wybierz programator NFC, aby zaprogramować nową kartę dostępu dla użytkownika.</label>
              {Array.isArray(nfcProgrammers) && nfcProgrammers.length !== 0 && 
              <select className='w-full p-2 border rounded-lg'  onChange={handleChangeSelectedProgrrammer}>
                <option  disabled >Wybierz programator NFC</option>
                {nfcProgrammers.map((programmer, id) => (     
                  <option key={programmer.uuid} value={programmer.uuid}>
                    Programator {id + 1}: {programmer.deviceStatus}
                  </option>
                ))}
              </select>}
                 <button className='bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition' onClick={handleCheckConnection}>Sprawdź połączenie</button>

            </Description>
            <div className="flex gap-4">
              <button className='bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition' onClick={handleProgramCard}>Programuj</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}