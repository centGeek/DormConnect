from flask import Flask, request
from pyzbar.pyzbar import decode
from PIL import Image
import io

app = Flask(__name__)

@app.route('/qr', methods=['POST'])
def read_qr():
    if 'image' not in request.files:
        return "Brak pliku 'image'", 400

    file = request.files['image']
    img = Image.open(file.stream)
    decoded = decode(img)

    if decoded:
        qr_data = decoded[0].data.decode('utf-8')
        return f"Zdekodowano: {qr_data}", 200
    else:
        return "Nie znaleziono QR", 200

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=False)
