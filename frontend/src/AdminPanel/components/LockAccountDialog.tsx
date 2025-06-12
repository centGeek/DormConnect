import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useState } from 'react'

export default function DeleteUserDialog(isLocked: boolean) {
  let [isOpen, setIsOpen] = useState(false)

  return (
    <>

      {!isLocked && 
        <div>
    <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={() => setIsOpen(true)}>Odblokuj konto użytkownika</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Odblokuj konto</DialogTitle>
            <Description> Czy na pewno chcesz odblokować konto użytkownika?</Description>
            <div className="flex gap-4">
              <button className='bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition' onClick={() => setIsOpen(false)}>Odblokuj</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
        </div>
      }

      {isLocked && 
      <div>
              <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={() => setIsOpen(true)}>Zablokuj konto użytkownika</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Zablokuj konto </DialogTitle>
            <Description>Czy na pewno chcesz zablokować konto użytkownika?</Description>
            <div className="flex gap-4">
              <button className='bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition' onClick={() => setIsOpen(false)}>Zablokuj</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </div>}

    </>
  )
}