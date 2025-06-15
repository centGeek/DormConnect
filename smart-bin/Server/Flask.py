from flask import Flask, request, jsonify, make_response
import time

app = Flask(__name__)

device_registered = False

@app.route('/api/program-card', methods=['POST'])
def program_device():
    global device_registered
    data = request.get_json()
    print("Received program request:", data)

    time.sleep(10)  # symulacja 10 sekund opóźnienia

    if not device_registered:
        device_registered = True
        print("Device programmed successfully")
        response = make_response(jsonify({"message": "Device programmed"}), 200)
    else:
        print("Device already programmed")
        response = make_response(jsonify({"message": "Already programmed"}), 200)

    # Dodaj nagłówek Connection: keep-alive
    response.headers["Connection"] = "keep-alive"
    return response

@app.route('/status', methods=['GET'])
def status():
    response = make_response(jsonify({"status": "online"}), 200)
    response.headers["Connection"] = "keep-alive"
    return response

if __name__ == '__main__':
    print("Starting mock server on http://localhost:9999")
    app.run(host='0.0.0.0', port=9999)
