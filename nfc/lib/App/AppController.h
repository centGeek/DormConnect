#include "../Web/WebServerController.h"

class AppController
{
private:
    uint8_t lockStatus;
    std::shared_ptr<SemaphoreHandle_t> pn532Semaphore = std::make_shared<SemaphoreHandle_t>();
    NfcController nfcController(pn532Semaphore);
    WebClientController webClientController(pn532Semaphore);
    WebServerController webServerController(pn532Semaphore);

public:
    AppController();
    uint8_t run();
    void mainLoop();
};


