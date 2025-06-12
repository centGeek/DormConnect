import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useState } from 'react'

export default function DeleteUserDialog() {
  let [isOpen, setIsOpen] = useState(false)

  return (
    <>
      <button className='bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition' onClick={() => setIsOpen(true)}>Generuj nowe UUID</button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">Zmiana UUID użytkownika</DialogTitle>
            <Description>Ta operacja jest nieodwracalna. Czy na pewno chcesz wygenerować nowe UUID?</Description>
            <div className="flex gap-4">
              <button className='bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition' onClick={() => setIsOpen(false)}>Generuj</button>
              <button className="bg-blue-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-blue-500 transition" onClick={() => setIsOpen(false)}>Anuluj</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </>
  )
}