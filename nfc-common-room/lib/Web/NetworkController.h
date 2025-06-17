#include "../../include/defines.h"
#include <WiFi.h>
#include <Arduino.h>


class NetworkController {
private:
    char* wifiSsid = WIFI_SSID;
    char* wifiPassword = WIFI_PASSWORD;
    IPAddress localIpAddress;
public:
    uint8_t connectToNetwork();
    IPAddress getLocalIpAddress() {
        return localIpAddress;
    }

};