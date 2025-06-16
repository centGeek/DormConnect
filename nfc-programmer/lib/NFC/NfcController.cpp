#include "NfcController.h"
#include <esp_task_wdt.h>

/**
 * @brief Converts a hexadecimal string to its uint8_t value.
 *
 * This function takes a String representing a hexadecimal number (e.g., "1A"),
 * converts it to a C-style string, and parses it as a base-16 (hexadecimal) integer.
 *
 * @param input The hexadecimal string to convert.
 * @return The uint8_t value corresponding to the hexadecimal input.
 */
uint8_t NfcController::stringToHex(String input)
{
    char charInput[input.length() + 1];
    input.toCharArray(charInput, sizeof(charInput));
    return (uint8_t) strtol(charInput, 0, 16);
}

/**
 * @brief Inserts a character at the specified index in the given String.
 *
 * This method creates a new String by inserting the specified character at the given index
 * of the base String. The original String is not modified.
 *
 * @param base The original String where the character will be inserted.
 * @param index The position at which to insert the character (0-based).
 * @param charToInsert The character to insert into the String.
 * @return A new String with the character inserted at the specified index.
 */
String NfcController::insertCharAt(String base, uint8_t index, char charToInsert)
{
    char previousChar = base.charAt(index);
    String firstHalf = base.substring(0, index);
    String secondHalf = base.substring(index);
    String finalString = firstHalf + charToInsert + secondHalf;
    return finalString;
}

NfcController::NfcController()
{
}

uint8_t NfcController::start_nfc()
{
    this->nfc.begin();
    uint32_t nfc_version = nfc.getFirmwareVersion();
    if (!nfc_version) {
        return 1;
    }
    if(!nfc.SAMConfig()) {
        return 1;
    }
    else {
        return 0;
    }
}

uint8_t NfcController::reset_nfc()
{
    this->nfc.reset();
    return 0;
}


uint8_t* NfcController::listen(uint16_t timeout)
{
    uint8_t found;
    static uint8_t uid[] = {0, 0, 0, 0, 0, 0, 0};
    uint8_t uid_length;

    uint8_t retryCount = 8;
    while(retryCount > 0) {
        found = this->nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uid_length, 1000);
        if (found) {
            Serial.println("NFC tag found!");
            Serial.print("UID Length: ");
            Serial.print(uid_length);
            Serial.println(" bytes");
            Serial.print("UID Value: ");
            for (size_t i = 0; i < uid_length; i++)
            {
                Serial.print(uid[i], HEX);
                Serial.print(" ");
            }
            Serial.println();
            break;
        } else {
            Serial.println("No NFC tag found, retrying...");
        }
        esp_task_wdt_reset();
        retryCount--;
        vTaskDelay(pdMS_TO_TICKS(100));
    }

    Serial.println(found);
    for(size_t i = 0; i < NTAG216_UID_SIZE; i++)
    {
        Serial.print(uid[i], HEX);
        Serial.print(" ");
    }
    Serial.println();
    if (found == 1)
    {
        Serial.println("returning uid");
        return uid;
    } 
    else {
        return nullptr;
    }
    
}

uint8_t NfcController::writeNfcUserUUID(String userUUID)
{

    // if (sizeof(this->checkNfcId()) == NTAG216_UID_SIZE) {
    //     return 2;
    // };

    // user id in nfc card memory:
    /*

    block 5     4 bytes of uuid
    block 6     4 bytes of uuid
    block 7     4 bytes of uuid
    block 8     4 bytes of uuid
    
    */
    uint8_t success = 0;
    uint8_t arrayCounter = 0;
    uint8_t *uuidArray = uuidToIntArray(userUUID);

    // writing 4 times 4-byte array - 16 bytes of uuid in summary
    for (size_t i = USER_UUID_START_PAGE; i < USER_UUID_START_PAGE + 4; i++)
    {

        uint8_t tempArray[] = {uuidArray[arrayCounter], uuidArray[arrayCounter + 1], uuidArray[arrayCounter + 2], uuidArray[arrayCounter + 3]};
        for (size_t z = 0; z < 4; z++)
        {
            Serial.print(tempArray[z], HEX);
        }
        Serial.println();

        success = this->nfc.mifareultralight_WritePage(i, tempArray);
        if (!success) {
            Serial.println("not ok");
            return -1;
        }
        arrayCounter += 4;
        Serial.println("ok");
    }
    
    return 0;
    
}


uint8_t* NfcController::readNfcUserUUID()
{

    uint8_t found;
    uint8_t uid[] = {0, 0, 0, 0, 0, 0, 0};
    uint8_t uid_length;

    found = this->nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uid_length, 1000);

    if (uid_length != NTAG216_UID_SIZE) {
        return nullptr;
    }

    uint8_t success = 0;
    uint8_t arrayCounter = 0;
    static uint8_t userUUID[4][4];
    static uint8_t uuidValue[16];

    for (size_t i = 5; i < 9; i++)
    {
        success = nfc.mifareultralight_ReadPage(i, userUUID[arrayCounter]);
        if(!success) {
            return nullptr;
        }
        arrayCounter++;
    }

    arrayCounter = 0;
    for (size_t i = 0; i < 4; i++)
    {
        for (size_t j = 0; j < 4; j++)
        {
            uuidValue[arrayCounter] = userUUID[i][j];
            arrayCounter++;
        }
        
    }
    return uuidValue;
}

uint8_t *NfcController::readCardUUID()
{
    uint8_t found;
    static uint8_t uid[] = {0, 0, 0, 0, 0, 0, 0};
    uint8_t uid_length;
    Serial.println("Reading...");
    found = this->nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uid_length, 5000);
    Serial.println(found);
    for (size_t i = 0; i < NTAG216_UID_SIZE; i++)
    {
        Serial.print(uid[i]);
    }
    
    if (found) {
        return uid;
    } else {
        return nullptr;
    }
}

String NfcController::uuidToString(uint8_t *uuid, uint8_t size)
{
    String finalUuidString;
    uint8_t uuidDashPositions[] = {8, 13, 18, 23};
    uint8_t positionsSize = 4;
    for (size_t i = 0; i < size; i++)
    {
        if(uuid[i] == 0) {
            finalUuidString += "00";
        }
        else {
            finalUuidString += String(uuid[i], HEX);
        }
    }
    for (size_t i = 0; i < positionsSize; i++)
    {
        finalUuidString = insertCharAt(finalUuidString, uuidDashPositions[i], '-');
    }
    
    return finalUuidString;
}

String NfcController::nfcTagToString(uint8_t* nfcUuid)
{
    String finalString = "";
    for (size_t i = 0; i < NTAG216_UID_SIZE; i++)
    {
        finalString += String(nfcUuid[i], HEX);
    }
    
    return finalString;
}

/**
 * Converts a UUID string to an array of 16 uint8_t values.
 *
 * This function takes a UUID string (typically in the format "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"),
 * parses it by removing hyphens, and converts each pair of hexadecimal characters into a uint8_t value.
 * The resulting 16-byte array is returned as a pointer to a static array.
 *
 * @param uuidString The UUID string to convert.
 * @return Pointer to a static array of 16 uint8_t values representing the UUID.
 *
 * @note The returned pointer refers to a static array, so its contents will be overwritten
 *       by subsequent calls to this function.
 */
uint8_t *NfcController::uuidToIntArray(String uuidString)
{
    static uint8_t finalUUID[16];
    int i = 0;
    int j = 0;
    while (i < uuidString.length()) {
        if (uuidString.substring(i, i + 1) == "-") i++;
        String firstPart =  uuidString.substring(i, i + 1);
        if (uuidString.substring(i + 1, i + 2) == "-") i++;
        String lastPart = uuidString.substring(i + 1, i + 2);
        String finalNumber = firstPart + lastPart;
        Serial.print("Final number: ");
        Serial.print(finalNumber);
        Serial.println();
        finalUUID[j] = stringToHex(finalNumber); 
        i+=2;
        j++;
    }
    return finalUUID;
}
