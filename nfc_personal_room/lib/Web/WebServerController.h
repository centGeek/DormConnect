#include "WiFi.h"
#include "AsyncTCP.h"
#include <ArduinoJson.h>
#include <ESPAsyncWebServer.h>
#include "../NFC/NfcController.h"
#include "WebClientController.h"

class WebServerController {

private:
    char* wifiSsid = "Redmi Note 10S";
    char* wifiPassword = "watxur6z45avvpy";
    uint16_t serverPort;
    std::shared_ptr<AsyncWebServer> webSemaphore;
    IPAddress localIpAddress;
    NfcController nfcController;
    WebClientController webClientController;
    AsyncWebServer* webServer;
    // only one service at time can access PN532 reader
    
    SemaphoreHandle_t nfcMutex;

public:
    WebServerController();
    ~WebServerController();
    uint8_t startServer();
    void stopServer();
    void initConnection();
    String getIpAddress();
    String getServerStatus();
    JsonDocument decodePostRequest(const char *data, size_t contentLength);
    bool validatePostRequestJsonData(JsonDocument document);
    uint8_t handleIncomingPostRequest(AsyncWebServerRequest *request, uint8_t *data);
};