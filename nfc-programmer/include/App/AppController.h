#include "../Web/WebServerController.h"
#include <NetworkController.h>
#include <LiquidCrystal_I2C.h>

#define LCD_COLS 16
#define LCD_ROWS 2

class AppController
{
private:
    uint8_t lockStatus;
    NfcController nfcController;
    WebClientController webClientController;
    WebServerController webServerController;
    NetworkController networkController;
    LiquidCrystal_I2C lcd;

public:
    AppController();
    uint8_t run();

};


