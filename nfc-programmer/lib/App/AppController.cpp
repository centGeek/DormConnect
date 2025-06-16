#include "AppController.h"


AppController::AppController() : 
    lcd(0x27, 16, 2)
{
    Serial.begin(9600);
    Wire.begin();
    pinMode(WORKING_OUTPUT_PIN, OUTPUT);

}

uint8_t AppController::run()
{

    delay(1000);
    uint8_t success = 0;

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
    Serial.println("Trying to register device...");
    webClientController.initialize();
    JsonDocument jsonData;
    jsonData["uuid"] = DEVICE_UUID;
    jsonData["port"] = SERVER_PORT;
    jsonData["ipAddress"] = networkController.getLocalIpAddress().toString();
    jsonData["deviceStatus"] = "online";
    do {
        Serial.println("Sending registration request...");
        vTaskDelay(pdTICKS_TO_MS(1000));
        uint8_t response = webClientController.sendHttpPostRequest(jsonData);
        if (response == 200) {
            Serial.println("Device registered successfully");
            break;
        } else {
            Serial.println("Error while registering device. Retrying in 3 seconds...");
            vTaskDelay(pdTICKS_TO_MS(3000));
        }
    }
    while(true);



    this->webServerController.startServer();
    
    return 0;
}
