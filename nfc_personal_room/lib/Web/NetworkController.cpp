#include "NetworkController.h"

uint8_t NetworkController::connectToNetwork()
{
    uint16_t connectCounter = 0;
    const TickType_t xDelay100Ms = pdMS_TO_TICKS(100);
    WiFi.mode(WIFI_STA);
    WiFi.begin(this->wifiSsid, this->wifiPassword);
    while (WiFi.status() != WL_CONNECTED)
    {
        vTaskDelay(xDelay100Ms);
        Serial.println("Connecting...");
        connectCounter++;
        if (connectCounter > 150) {
            Serial.println("Could not connect to the network... retrying in 3 seconds");
            connectCounter = 0;
            vTaskDelay(pdTICKS_TO_MS(3000));
        }
    }
    
    Serial.print("Device connected to the network. Current ip address: ");
    Serial.print(WiFi.localIP());
    Serial.println();
    Serial.print("Gateway: ");
    Serial.print(WiFi.gatewayIP());
    Serial.println();
    this->localIpAddress = WiFi.localIP();
    return 0;
}