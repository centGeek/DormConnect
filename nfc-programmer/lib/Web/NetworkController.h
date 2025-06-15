#include "../../include/defines.h"
#include <WiFi.h>
#include <Arduino.h>


class NetworkController {
private:
    char* wifiSsid = "Redmi Note 10S"; 
    char* wifiPassword = "watxur6z45avvpy";
    IPAddress localIpAddress;
public:
    uint8_t connectToNetwork();

};