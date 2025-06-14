#include "WebServerController.h"



WebServerController::WebServerController()
{

    this->serverPort = 8085;
    this->webServer = new AsyncWebServer(serverPort);
    this->webSemaphore = webSemaphore;
}

WebServerController::~WebServerController()
{
    
}

/**
 * @brief Initializes and starts the web server, setting up API endpoints.
 *
 * This method configures the web server with the following endpoints:
 * - GET /api/wifi-inwo: Returns a JSON object containing the WiFi status, SSID, and local IP address.
 *   Introduces a 2-second delay before sending the response.
 * - POST /api/post-test: Accepts a JSON payload containing "serverId", "roomNumber", "userUUID", and "authorizationstatus".
 *   Responds with a JSON object containing "deviceId", "roomNumber", "userUUID", and "authorizationStatus" if all keys are present.
 *   Returns an error if the content type is not "application/json" or if required keys are missing.
 *
 * @return uint8_t Returns 0 upon successful server initialization.
 */
uint8_t WebServerController::startServer()
{
    Serial.println("Server started");
    this->webServer->on("/api/wifi-inwo", HTTP_GET, [](AsyncWebServerRequest *request) {
        AsyncResponseStream *response = request->beginResponseStream("application/json");
        JsonDocument json;
        json["status"] = "ok";
        json["ssid"] = WiFi.SSID();
        json["ip"] = WiFi.localIP().toString();
        Serial.println("Sending response... delay for 2s");
        delay(2000);
        Serial.println("End the delay");
        serializeJson(json, *response);
        request->send(response);
    });

    this->webServer->on("/api/post-test", HTTP_POST, [](AsyncWebServerRequest *request) {
        // Serial.print("Content type: ");
        // Serial.println(request->contentType());
        // //AsyncResponseStream *response = request->beginResponseStream();

        // if (request->contentType() == "application/json") {
        //     Serial.println(request->params());
        //     Serial.println(request->args());
        //     Serial.println(request->contentLength());
            
        //     Serial.println(*request);
        // }


        // uint8_t params = request->params();
        // Serial.printf("%d params sent in\n", params);
        // for (int i = 0; i < params; i++)
        // {
        //     AsyncWebParameter *p = request->getParam(i);
        //     if (p->isFile())
        //     {
        //         Serial.printf("_FILE[%s]: %s, size: %u", p->name().c_str(), p->value().c_str(), p->size());
        //     }
        //     else if (p->isPost())
        //     {
        //         Serial.printf("%s: %s \n", p->name().c_str(), p->value().c_str());
        //     }
        //     else
        //     {
        //         Serial.printf("_GET[%s]: %s", p->name().c_str(), p->value().c_str());
        //     }
        // }
    
    }, NULL, [](AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total) {


        AsyncResponseStream *response = request->beginResponseStream("application/json");
        if (request->contentType() != "application/json") {
            request->send(500, "text", "bad format");
        }


        Serial.println("Deserializing json...");
        JsonDocument document;
        deserializeJson(document, (const char *)data, request->contentLength());

        const char* serverId;
        const char* roomNumber;
        const char* userUUID;
        const char* authorizationStatus;

        if (document.containsKey("serverId")
        && document.containsKey("serverId")
        && document.containsKey("serverId")
        && document.containsKey("serverId")) {

            serverId = document["serverId"];
            roomNumber = document["roomNumber"];
            userUUID = document["userUUID"];
            authorizationStatus = document["authorizationstatus"];

            JsonDocument responseJson;
            responseJson["deviceId"] = "test-uuid";
            responseJson["roomNumber"] = roomNumber;
            responseJson["userUUID"] = userUUID;
            Serial.println("ok");
            responseJson["authorizationStatus"] = authorizationStatus;
            serializeJson(responseJson, *response);
            request->send(response);
        } 

        else {
            request->send(500, "text", "cannot find all json keys");
        }



    });

    this->webServer->begin();
    Serial.println("Server initialized");
    return 0;
}

void WebServerController::stopServer()
{
    return this->webServer->end();
}


String WebServerController::getIpAddress()
{
    return WiFi.localIP().toString();
}

String WebServerController::getServerStatus()
{
    return "ebe";
}

