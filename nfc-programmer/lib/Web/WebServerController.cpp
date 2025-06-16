#include "WebServerController.h"
#include <esp_task_wdt.h>

WebServerController::WebServerController()
{

    this->serverPort = SERVER_PORT;
    this->webServer = new AsyncWebServer(serverPort);
    this->nfcController = NfcController();
}

WebServerController::~WebServerController()
{
}

uint8_t WebServerController::startServer()
{
    this->nfcController.start_nfc();
    Serial.println("Server started");
    this->webServer->on("/api/wifi-info", HTTP_GET, [](AsyncWebServerRequest *request)
                        {
        AsyncResponseStream *response = request->beginResponseStream("application/json");
        JsonDocument json;
        json["status"] = "ok";
        json["ssid"] = WiFi.SSID();
        json["ip"] = WiFi.localIP().toString();
        serializeJson(json, *response);
        request->send(response); });

    this->webServer->on("/api/program-card", HTTP_POST, [](AsyncWebServerRequest *request) {

    },
                        NULL, [this](AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total)
                        {
                            Serial.println("Received request to program card");
                            digitalWrite(WORKING_OUTPUT_PIN, HIGH);
                            AsyncResponseStream *response = request->beginResponseStream("application/json");
                            response->addHeader("Connection", "keep-alive");

                            if (request->contentType() != "application/json")
                            {
                                Serial.println("Received request with wrong content type");
                                digitalWrite(WORKING_OUTPUT_PIN, LOW);
                                request->send(500, "text", "bad format");
                            }

                            Serial.println("Deserializing json...");
                            JsonDocument document;
                            deserializeJson(document, (const char *)data, request->contentLength());

                            const char *serverId;
                            const char *roomNumber;
                            const char *userUUID;
                            const char *authorizationStatus;

                            if (document.containsKey("userUuid"))
                            {

                                userUUID = document["userUuid"];
                                String uuidString = String(userUUID);

                                Serial.print("received userUUID: ");
                                Serial.println(userUUID);

                                Serial.println("Programming card...");

                                uint8_t *cardUid;

                                cardUid = this->nfcController.listen();

                                if (cardUid == nullptr)
                                {
                                    esp_task_wdt_reset();
                                    Serial.println("Timeout while listening for NFC card");
                                    digitalWrite(WORKING_OUTPUT_PIN, LOW);
                                    request->send(500, "Could not read nfc card");

                                }
                                else
                                {
                                    uint8_t success = this->nfcController.writeNfcUserUUID(uuidString);
                                    if (success != 0)
                                    {
                                        Serial.println("Error while writing NFC card");
                                        digitalWrite(WORKING_OUTPUT_PIN, LOW);
                                        request->send(500, "Could not write nfc card");
                                        return;
                                    }

                                    Serial.println("Card programmed");

                                    JsonDocument responseJson;
                                    responseJson["deviceId"] = DEVICE_UUID;
                                    responseJson["userUuid"] = userUUID;
                                    responseJson["cardUuid"] = this->nfcController.nfcTagToString(cardUid);
                                    Serial.println("ok");
                                    serializeJson(responseJson, *response);
                                    response->setCode(200);
                                    digitalWrite(WORKING_OUTPUT_PIN, LOW);
                                    request->send(response);
                                }
                            }

                            else
                            {
                                Serial.println("Cannot find all json keys");
                                digitalWrite(WORKING_OUTPUT_PIN, LOW);
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
