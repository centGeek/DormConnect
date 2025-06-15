#include "WebClientController.h"

WebClientController::WebClientController()
{
}

uint8_t WebClientController::initialize()
{
    return 0;
}

uint8_t WebClientController::sendHttpPostRequest(JsonDocument jsonData)
{
    this->http.begin(SERVER_REQUEST_ADDRESS);
    this->http.addHeader("Content-Type", "application/json");
    String dataToSend;
    serializeJson(jsonData, dataToSend);    
    uint8_t httpResponse = http.POST(dataToSend);
    if (httpResponse > 0) {
        String payload = http.getString();
        Serial.println(httpResponse);
        Serial.println(payload);
    }

    http.end();
    return httpResponse;
}

uint8_t WebClientController::sendHttpGetRequest(String url) {
    this->http.begin(SERVER_REQUEST_ADDRESS);
    uint8_t httpResponse = http.GET();
    if (httpResponse > 0) {
        String payload = http.getString();
        Serial.println(httpResponse);
        Serial.println(payload);
    }

    http.end();
    return httpResponse;
}