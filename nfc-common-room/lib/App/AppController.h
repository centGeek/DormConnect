#include <NetworkController.h>
#include <NfcController.h>
#include <WebClientController.h>
#include <Arduino.h>

#define LCD_COLS 16
#define LCD_ROWS 2

class AppController
{
private:
    uint8_t lockStatus;
    NfcController nfcController;
    WebClientController webClientController;
    NetworkController networkController;
    // void IRAM_ATTR buttonInterruptHandler();


public:
    AppController();
    ~AppController();
    void initialize();
    uint8_t run();
    void mainLoopTask();


};


