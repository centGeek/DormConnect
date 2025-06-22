#include "WebClientController.h"

WebClientController::WebClientController()
{
}



/**
 * @brief Sends an HTTP POST request with JSON data to the specified URL.
 *
 * This method serializes the provided JSON document and sends it as the body of an HTTP POST request
 * to the given URL. The "Content-Type" header is set to "application/json". If the request is successful,
 * the HTTP response code and payload are printed to the serial output.
 *
 * @param jsonData The JSON document to be sent in the POST request body.
 * @param url The destination URL for the POST request.
 * @return uint8_t The HTTP response code returned by the server. Returns 0 if the request fails to connect.
 */
uint8_t WebClientController::sendHttpPostRequest(JsonDocument jsonData, String url)
{
    this->http.begin(url);
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

/**
 * @brief Sends an HTTP GET request to the specified URL.
 *
 * This method initializes an HTTP connection to the provided URL and sends a GET request.
 * If the request is successful (response code > 0), it retrieves and prints the response payload
 * and HTTP status code to the serial output. The HTTP connection is properly closed after the request.
 *
 * @param url The URL to which the HTTP GET request will be sent.
 * @return uint8_t The HTTP response code received from the server.
 */
uint8_t WebClientController::sendHttpGetRequest(String url) {
    this->http.begin(url);
    uint8_t httpResponse = http.GET();
    if (httpResponse > 0) {
        String payload = http.getString();
        Serial.println(httpResponse);
        Serial.println(payload);
    }

    http.end();
    return httpResponse;
}