import { Description, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'

export default function SuccessDialog({
  open,
  message,
}: {
  open: boolean,
  onClose: () => void,
  message: string
}) {

    const handleClose = () => {
        window.location.reload();
    }
  return (
    <Dialog open={open} onClose={handleClose} className="relative z-50">
      <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
        <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
          <DialogTitle className="font-bold">Operacja zakończona powodzeniem</DialogTitle>
          <Description>
            <label className="block mb-2 text-sm font-medium text-gray-700">
              Operacja zakończona pomyślnie: {message}
            </label>
          </Description>
          <div className="flex gap-4">
            <button
              type="button"
              className="bg-green-600 m-2 text-white px-5 py-2 rounded-lg hover:bg-green-500 transition"
              onClick={handleClose}
            >
              OK
            </button>
          </div>
        </DialogPanel>
      </div>
    </Dialog>
  )
}