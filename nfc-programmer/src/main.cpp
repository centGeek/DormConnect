#include <Wire.h>
#include <Adafruit_PN532.h>

#include "../lib/App/AppController.h"

#define SDA_PIN 21
#define SCL_PIN 22

AppController appController;

const char* testUUID = "e58ed763-928c-4155-bee9-fdbaaadc15f3";


void setup() {

    
    delay(2000);
    Serial.println("ok");
    
    appController.run();
    // lcd.init(SDA_PIN, SCL_PIN);                      // initialize the lcd 
    // // Print a message to the LCD.
	// lcd.backlight();
	// lcd.setCursor(3,0);
	// lcd.print("Hello, world!");
	// lcd.setCursor(2,1);
	// lcd.print("Time is now");


    // webServerController.connectToNetwork();
    // webServerController.startServer();
    // JsonDocument document;
    // document["siema"] = "witam";

    // delay(3000);
    // uint8_t response = webClientController.sendHttpPostRequest(document);
    // Serial.print("Response: ");
    // Serial.println(response);

    // String uuid = "e58ed763-928c-4155-bee9-fdbaaadc15f3";
    // uint8_t* uuidArray = nfcController.uuidToIntArray(uuid);
    // for (size_t i = 0; i < 16; i++)
    // {
    //     Serial.print(uuidArray[i], HEX);
    //     Serial.print(" ");
    // }
    // Serial.println();

    // String fromUuidArray = nfcController.uuidToString(uuidArray, 16);
    // Serial.println(fromUuidArray);
    // Serial.println(uuid.equals(fromUuidArray));
    
}

void loop() {

}