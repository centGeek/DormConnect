#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "../../include/defines.h"

class WebClientController {

private:
    HTTPClient http;

public:
    WebClientController();
    uint8_t initialize();
    uint8_t sendHttpPostRequest(JsonDocument jsonData);
    uint8_t sendHttpGetRequest(String url);

};