#include "NetworkController.h"

/**
 * @brief Attempts to connect the device to a WiFi network using the configured SSID and password.
 *
 * This function sets the WiFi mode to station, initiates the connection, and repeatedly checks the connection status.
 * If the connection is not established within a certain number of attempts (approximately 15 seconds), it waits for 3 seconds before retrying.
 * The process repeats until a connection is successfully established.
 * Upon successful connection, the device's local IP address and gateway are printed to the serial output.
 * The local IP address is also stored in the class member variable.
 *
 * @return uint8_t Returns 0 upon successful connection.
 */
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