#include "WiFi.h"
#include "AsyncTCP.h"
#include <ArduinoJson.h>
#include <ESPAsyncWebServer.h>
#include "../NFC/NfcController.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/semphr.h"

class WebServerController {

private:
    char* wifiSsid = WIFI_SSID;
    char* wifiPassword = WIFI_PASSWORD;
    uint16_t serverPort;
    std::shared_ptr<AsyncWebServer> webSemaphore;
    IPAddress localIpAddress;
    NfcController nfcController;
    AsyncWebServer* webServer;
    xSemaphoreHandle nfcMutex;

public:
    WebServerController();
    ~WebServerController();
    uint8_t startServer();
    void stopServer();
    String getIpAddress();
    String getServerStatus();
    JsonDocument decodePostRequest(const char *data, size_t contentLength);
    bool validatePostRequestJsonData(JsonDocument document);
    uint8_t handleIncomingPostRequest(AsyncWebServerRequest *request, uint8_t *data);
};