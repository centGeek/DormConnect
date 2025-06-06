#include "AppController.h"


AppController::AppController() : 
    lcd(0x27, 16, 2)
{
    Serial.begin(9600);
    Wire.begin();
    webClientController.initialize();


}

uint8_t AppController::run()
{

    delay(1000);
    uint8_t success = 0;

    // nfc initialization
    do {
        success = this->nfcController.start_nfc();
        if (success) {
            Serial.println("Cannot find nfc device, retrying...");
            vTaskDelay(pdTICKS_TO_MS(1000));
        }
    } while (success);

    lcd.init(16, 2);
    lcd.backlight();
    lcd.setContrast(80);
    lcd.clear();
    delay(1000);
    lcd.setCursor(0,0);
    lcd.print("NFC initialized");

    // network initialization
    success = networkController.connectToNetwork();
    if (success) {
        Serial.println("Error while connecting to network. Resetting module...");
        vTaskDelay(pdTICKS_TO_MS(1000));
    }
    lcd.setCursor(0, 1);
    lcd.print("Connected to network");
    vTaskDelay(pdTICKS_TO_MS(1000));
    //nfcController.writeNfcUserUUID("3cd1be9e-e9a6-4aa4-b97b-1a8934bb828a");
    mainLoopTask();
    
    return 0;
}

void AppController::mainLoopTask()
{
    uint8_t *readNfcTag;
    
    while (true)
    {
        Serial.println("Listening...");
        readNfcTag = this->nfcController.readCardUUID();
        if (readNfcTag != nullptr)
        {
            //this->webServerController.stopServer();
            uint8_t *userUUID = nfcController.readNfcUserUUID();
            Serial.println("Success");
            Serial.print("Found user with id: ");
            Serial.println(this->nfcController.uuidToString(userUUID, 16));
            Serial.print("NFC card ID: ");
            String nfcTagString = nfcController.nfcTagToString(readNfcTag);
            Serial.println(nfcTagString);
            Serial.println("Sending http request...");

            JsonDocument json;
            json["device_uuid"] = DEVICE_UUID;
            json["card_uid"] = nfcTagString;
            json["user_uuid"] = "3cd1be9e-e9a6-4aa4-b97b-1a8934bb828a"; //nfcController.uuidToString(userUUID, USER_UUID_SIZE_BYTES);
            json["roomNumber"] = ROOM_NUMBER;
            json["lockStatus"] = "UNKNOWN";
            uint8_t response = webClientController.sendHttpPostRequest(json);
            if (response == 200)
            {
                lcd.clear();
                lcd.setCursor(0,0);
                lcd.print("ACCESS GRANTED");
            } else if (response == 403) {
                lcd.clear();
                lcd.setCursor(0,0);
                lcd.print("ACCESS DENIED");
            } else {
                lcd.clear();
                lcd.setCursor(0,0);
                lcd.print("CONNECTION ERROR");
            }
            

            vTaskDelay(pdMS_TO_TICKS(1000));
            //this->webServerController.startServer();
        }

        else
        {
            Serial.println("Error while reading nfc tag...");
            vTaskDelay(pdMS_TO_TICKS(1000));
        }
    }
}
