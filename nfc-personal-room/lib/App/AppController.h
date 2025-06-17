#include <NetworkController.h>
#include <LiquidCrystal_I2C.h>
#include <NfcController.h>
#include <WebClientController.h>

#define LCD_COLS 16
#define LCD_ROWS 2

class AppController
{
private:
    uint8_t lockStatus;
    NfcController nfcController;
    WebClientController webClientController;
    NetworkController networkController;
    void mainLoopTask();

public:
    AppController();
    uint8_t run();

};


