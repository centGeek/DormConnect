import cv2
import pyzbar.pyzbar as pyzbar

url = 'http://192.168.237.126/stream'
cap = cv2.VideoCapture(url)

prev_data = ""

while True:
    ret, frame = cap.read()
    if not ret:
        print("Brak klatki, ponowne otwarcie streamu")
        cap.release()
        cap = cv2.VideoCapture(url)
        continue

    decodedObjects = pyzbar.decode(frame)
    for obj in decodedObjects:
        data = obj.data.decode('utf-8')
        if data != prev_data:
            print("Typ:", obj.type)
            print("Dane:", data)
            prev_data = data
        cv2.putText(frame, data, (50, 50), cv2.FONT_HERSHEY_PLAIN, 2, (255, 0, 0), 3)

    cv2.imshow("live transmission", frame)
    if cv2.waitKey(1) == 27:
        break

cap.release()
cv2.destroyAllWindows()
