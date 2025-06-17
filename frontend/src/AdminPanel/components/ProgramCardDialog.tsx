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
  const [loading, setLoading] = useState(false);
  const [connectionStatus, setConnectionStatus] = useState(false);
  const [connectionRequest, setConnectionRequest] = useState(false);
  const [isCardProgrammingInProgress, setIsCardProgrammingInProgress] = useState(false);

    const handleCheckConnection = async () => {
    if (!selectedProgrammer) {
      console.error("no nfc programmer selected")
      
    }
    
    try {
      setLoading(true);
      console.log("Checking connection for NFC programmer: ", selectedProgrammer?.uuid);
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
        setConnectionRequest(true);
        setConnectionStatus(true);
        setLoading(false);
      } else {
        console.error("Connection check failed: ", response);
        setConnectionRequest(true);
        setConnectionStatus(false);
        setLoading(false);
      }

    } catch (error) {
      console.error("An error occured while checking connection: ", error )
      setConnectionRequest(true);
      setConnectionStatus(false);
      setLoading(false);
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
    setIsCardProgrammingInProgress(true);
    if (!selectedProgrammer) {
      console.error("No NFC programmer selected");
      setIsCardProgrammingInProgress(false);
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
      setIsCardProgrammingInProgress(false);
      if (response.status === HttpStatusCode.Ok) {
        console.log("Card programmed successfully: ", response.data);
        
        if (onSuccess) {
          
          onSuccess("Karta dostępu została zaprogramowana pomyślnie.");
        }
        setIsOpen(false); // Close the dialog on success
      }

    } catch (error) {
      setIsCardProgrammingInProgress(false);
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

  const handleCloseConnectionInfo = () => {
    setConnectionRequest(false);
    setConnectionStatus(false);
    setLoading(false);
  }

    const handleSetOpen = () => {
      setIsOpen(true);
      handleFetchAvaliableProgrammers();
    }

    const handleCloseDialog = () => {
      setIsCardProgrammingInProgress(false);
      setIsOpen(false);
      setConnectionRequest(false);
      setConnectionStatus(false);
      setLoading(false);
    }


  return (
    <>
      <button className='w-40  m-2 bg-gray-600 text-white hover:bg-gray-500 px-5 py-2 rounded-lg  transition' onClick={handleSetOpen}>Zaprogramuj kartę dostępu</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
            <DialogTitle className="font-bold text-2xl">Programowanie karty dostępu</DialogTitle>
            <Description>
              <label className='text-gray-600'>Wybierz programator NFC, aby zaprogramować nową kartę dostępu dla użytkownika.</label>
              {Array.isArray(nfcProgrammers) && nfcProgrammers.length !== 0 && 
              <select className='w-full p-2 border rounded-lg mt-3 mb-3'  onChange={handleChangeSelectedProgrrammer}>
                <option  disabled >Wybierz programator NFC</option>
                {nfcProgrammers.map((programmer, id) => (     
                  <option key={programmer.uuid} value={programmer.uuid}>
                    Programator {id + 1}: {programmer.ipAddress}
                  </option>
                ))}
              </select>}
                <div className="flex justify-center">
                  <button className='bg-gray-600 w-fit text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleCheckConnection}>
                    Sprawdź połączenie
                  </button>
                </div>
                {loading && <label className= 'text-gray-600 flex flex-col items-center'>Sprawdzanie połączenia z programatorem...</label>}
                {connectionRequest && connectionStatus && 
                <div className='flex flex-col items-center'>
                <label className='text-gray-600'>Połączenie z programatorem zostało nawiązane.</label>
                <button onClick={handleCloseConnectionInfo} className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition'>ok</button>
                </div>}

                {connectionRequest && !connectionStatus && 
                <div className='flex flex-col items-center'>
                <label className='text-gray-600'>Nie udało się nawiązać połączenia z programatorem.</label>
                <button onClick={handleCloseConnectionInfo} className='bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition'>ok</button>
                </div>}
            </Description>
            <div className="flex gap-4">
              <div className="flex w-full justify-between">
                <button className='bg-gray-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-gray-500 transition' onClick={handleProgramCard}>Programuj</button>
                <button className="bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
              </div>
            </div>
            {isCardProgrammingInProgress && 
            <div className='flex flex-col items-center'>
            <label className='text-gray-600'>Programowanie karty ...</label>
            </div>}
            
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}