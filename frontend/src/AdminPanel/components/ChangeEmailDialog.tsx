import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useState } from 'react'

export default function ChangEmailDialog() {
  let [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={() => setIsOpen(true)}>Zmień adres e-mail</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <form>
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">

          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Zmiana adresu e-mail użytkownika</DialogTitle>
            <Description>
                <label className="block mb-2 text-sm font-medium text-gray-700">Wprowadź nowy adres e-mail:</label>
                <input type="email" className="block w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Wpisz nowy adres e-mail"/>

            </Description>
            <div className="flex gap-4">
              <button className='bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition' onClick={() => setIsOpen(false)}>Zmień</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>

            </div>  
          </DialogPanel>
        </div>
        </form>
      </Dialog>
    </>
  )
}