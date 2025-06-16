#include "../Web/WebServerController.h"
#include <NetworkController.h>
#include <LiquidCrystal_I2C.h>
#include <WebClientController.h>

#define LCD_COLS 16
#define LCD_ROWS 2

class AppController
{
private:
    WebServerController webServerController;
    NetworkController networkController;
    WebClientController webClientController;
    LiquidCrystal_I2C lcd;

public:
    AppController();
    uint8_t run();
};


