#include "../Web/WebServerController.h"
#include <NetworkController.h>
#include <WebClientController.h>

class AppController
{
private:
    WebServerController webServerController;
    NetworkController networkController;
    WebClientController webClientController;

public:
    AppController();
    uint8_t run();
};


