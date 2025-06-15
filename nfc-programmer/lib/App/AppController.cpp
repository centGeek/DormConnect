#include "AppController.h"


AppController::AppController() : 
    lcd(0x27, 16, 2)
{
    Serial.begin(9600);
    Wire.begin();


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
    this->webServerController.startServer();
    
    return 0;
}
