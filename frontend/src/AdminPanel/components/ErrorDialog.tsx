import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'

export default function ErrorDialog({
  open,
  onClose,
  message,
}: {
  open: boolean,
  onClose: () => {},
  message: string
}) {

  return (
    <Dialog open={open} onClose={onClose} className="relative z-50">
      <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
        <DialogPanel className="max-w-lg space-y-4 border-4 bg-white p-12 rounded-lg w-300">
          <DialogTitle className="font-bold text-2xl">Wystąpił błąd</DialogTitle>
          <Description>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Wystąpił błąd: {message}
            </label>
          </Description>
          <div className="flex gap-4">
            <button
              type="button"
              className="bg-red-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-red-500 transition"
              onClick={onClose}
            >
              OK
            </button>
          </div>
        </DialogPanel>
      </div>
    </Dialog>
  )
}