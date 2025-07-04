#include "AppController.h"

AppController::AppController()
{
    Serial.begin(9600);
    pinMode(WORKING_OUTPUT_PIN, OUTPUT);
    pinMode(LOCK_OUTPUT_PIN, OUTPUT);
    digitalWrite(WORKING_OUTPUT_PIN, HIGH);
    this->lockStatus = 1; // 1 - open, 0 - closed
}

uint8_t AppController::run()
{

    delay(1000);
    uint8_t success = 0;

    // nfc initialization
    do
    {
        success = this->nfcController.start_nfc();
        if (success)
        {
            Serial.println("Cannot find nfc device, retrying...");
            vTaskDelay(pdTICKS_TO_MS(1000));
        }
    } while (success);

    // network initialization
    success = networkController.connectToNetwork();
    if (success)
    {
        Serial.println("Error while connecting to network. Resetting module...");
        vTaskDelay(pdTICKS_TO_MS(1000));
    }


    // REGISTER DEVICE
    vTaskDelay(pdTICKS_TO_MS(1000));
    // nfcController.writeNfcUserUUID("3cd1be9e-e9a6-4aa4-b97b-1a8934bb828a");
    Serial.println("Trying to register device...");

    JsonDocument jsonData;
    jsonData["uuid"] = DEVICE_UUID;
    jsonData["roomNumber"] = ROOM_NUMBER;
    jsonData["ipAddress"] = networkController.getLocalIpAddress().toString();
    jsonData["deviceStatus"] = "online";
    jsonData["lockStatus"] = "OPEN";
    jsonData["macAddress"] = WiFi.macAddress().c_str();
    do
    {
        Serial.println("Sending registration request...");
        vTaskDelay(pdTICKS_TO_MS(1000));
        uint8_t response = webClientController.sendHttpPostRequest(jsonData, SERVER_REGISTER_DEVICE_ADDRESS);
        if (response == 201)
        {
            Serial.println("Device registered successfully");
            break;
        }
        else
        {
            Serial.println("Error while registering device. Retrying in 3 seconds...");
            vTaskDelay(pdTICKS_TO_MS(3000));
        }
    } while (true);

    // nfcController.writeNfcUserUUID("3cd1be9e-e9a6-4aa4-b97b-1a8934bb828a");
    mainLoopTask();

    return 0;
}

void AppController::mainLoopTask()
{
    uint8_t *readNfcTag;

    while (true)
    {
        Serial.println("Listening...");
        readNfcTag = this->nfcController.listen();
        if (readNfcTag != nullptr)
        {

                digitalWrite(WORKING_OUTPUT_PIN, HIGH);
                // this->webServerController.stopServer();
                uint8_t *userUUID = nfcController.readNfcUserUUID();

                if (userUUID == nullptr)
                {
                    Serial.println("Error while reading user UUID from NFC tag");

                    vTaskDelay(pdMS_TO_TICKS(2000));
                    digitalWrite(WORKING_OUTPUT_PIN, LOW);
                    continue;
                }
                Serial.println("Success");
                Serial.print("Found user with id: ");
                String userUUIDString = nfcController.uuidToString(userUUID, USER_UUID_SIZE_BYTES);
                Serial.println(userUUIDString);
                Serial.print("NFC card ID: ");
                String nfcTagString = nfcController.nfcTagToString(readNfcTag);
                Serial.println(nfcTagString);
                Serial.println("Sending http request...");

                JsonDocument json;
                json["deviceUuid"] = DEVICE_UUID;
                json["cardUid"] = nfcTagString;
                json["userUuid"] = userUUIDString; // nfcController.uuidToString(userUUID, USER_UUID_SIZE_BYTES);
                json["roomNumber"] = ROOM_NUMBER;
                json["lockStatus"] = "UNKNOWN";
                uint8_t response = webClientController.sendHttpPostRequest(json, SERVER_REQUEST_ADDRESS);
                Serial.print("Response code: ");
                Serial.println(response);

                if (response == 200)
                {
                    Serial.println("Access granted");
                    if (this->lockStatus == 0)
                    {
                        Serial.println("Locking the door...");
                        digitalWrite(LOCK_OUTPUT_PIN, HIGH);
                        this->lockStatus = 1;
                    }
                    else
                    {
                        Serial.println("Unlocking the door...");
                        digitalWrite(LOCK_OUTPUT_PIN, LOW);
                        this->lockStatus = 0;
                    }
                }

                vTaskDelay(pdMS_TO_TICKS(2000));
                digitalWrite(WORKING_OUTPUT_PIN, LOW);
                // this->webServerController.startServer();
            

        }

        else
        {
            Serial.println("Error while reading nfc tag...");
            vTaskDelay(pdMS_TO_TICKS(1000));
        }
    }
}
