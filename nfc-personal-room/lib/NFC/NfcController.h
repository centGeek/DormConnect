#include "Adafruit_PN532.h"
#include "../../include/defines.h"
#include <memory>

class NfcController {

private:
    Adafruit_PN532 nfc = Adafruit_PN532(SDA_PIN, SCL_PIN);
    uint8_t stringToHex(String input);
    String insertCharAt(String base, uint8_t index, char charToInsert);
    std::shared_ptr<SemaphoreHandle_t> pn532Semaphore;

public:
    NfcController();
    NfcController(std::shared_ptr<SemaphoreHandle_t> semaphorePointer);
    uint8_t start_nfc();
    uint8_t reset_nfc(); 
    // returns: uid of the read nfc tag
    uint8_t* listen();
    uint8_t writeNfcUserUUID(String userUUID);
    uint8_t *readNfcUserUUID();
    uint8_t *checkNfcId();
    uint8_t *readCardUUID();
    String uuidToString(uint8_t* uuid, uint8_t size);
    // default size:     NTAG216_UID_SIZE
    String nfcTagToString(uint8_t* nfcUuid);
    uint8_t *uuidToIntArray(String uuidString);
};