#include "NfcController.h"

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
    return (uint8_t)strtol(charInput, 0, 16);
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

/**
 * @brief Initializes the NFC controller and prepares it for communication.
 *
 * This function begins communication with the NFC hardware, checks the firmware version,
 * and configures the Secure Access Module (SAM). It returns a status code indicating
 * whether the initialization was successful.
 *
 * @return uint8_t Returns 0 on successful initialization, 1 on failure.
 */
uint8_t NfcController::start_nfc()
{
    this->nfc.begin();
    uint32_t nfc_version = nfc.getFirmwareVersion();
    if (!nfc_version)
    {
        return 1;
    }
    if (!nfc.SAMConfig())
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

uint8_t NfcController::reset_nfc()
{
    this->nfc.reset();
    return 0;
}

/**
 * @brief Listens for an NFC tag and retrieves its UID if found.
 *
 * This function attempts to read a passive NFC target using the PN532 module.
 * If a tag is detected, it returns a pointer to a static array containing the UID.
 * If no tag is found or an exception occurs, it returns nullptr.
 *
 * @return uint8_t* Pointer to the UID array if a tag is found, nullptr otherwise.
 */
uint8_t *NfcController::listen()
{
    uint8_t found;
    static uint8_t uid[] = {0, 0, 0, 0, 0, 0, 0};
    uint8_t uid_length;
    try
    {
        found = this->nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uid_length);
        if (found)
        {
            return uid;
        }
        else
        {
            return nullptr;
        }
    }
    catch (const std::exception &e)
    {
        Serial.print("Exception: ");
        Serial.println(e.what());
        return nullptr;
    }
}

/**
 * @brief Writes a 16-byte user UUID to an NFC card.
 *
 * This function takes a user UUID in string format, converts it to a 16-byte array,
 * and writes it to four consecutive 4-byte blocks (pages) in the NFC card's memory,
 * starting from USER_UUID_START_PAGE. Each block receives 4 bytes of the UUID.
 *
 * @param userUUID The user UUID as a String (expected to be 16 bytes when converted).
 * @return uint8_t Returns 0 on success, or -1 if writing to the NFC card fails.
 */
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
        if (!success)
        {
            Serial.println("not ok");
            return -1;
        }
        arrayCounter += 4;
        Serial.println("ok");
    }

    return 0;
}

/**
 * @brief Reads the UUID of an NFC user from an NTAG216 tag.
 *
 * This function attempts to detect an NFC tag and read its user UUID.
 * It first checks for the presence of a tag with the expected UID size.
 * If a valid tag is found, it reads four consecutive pages (5 to 8) from the tag,
 * each containing 4 bytes, and concatenates them into a 16-byte UUID array.
 *
 * @return Pointer to a static 16-byte array containing the user UUID if successful,
 *         or nullptr if the tag is not found, the UID size is incorrect, or a read fails.
 */
uint8_t *NfcController::readNfcUserUUID()
{

    uint8_t found;
    static uint8_t uid[] = {0, 0, 0, 0, 0, 0, 0};
    uint8_t uid_length;

    found = this->nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uid_length);

    if ((found == false) || (uid_length != NTAG216_UID_SIZE))
    {
        return nullptr;
    }

    uint8_t success = 0;
    uint8_t arrayCounter = 0;
    static uint8_t userUUID[4][4];
    static uint8_t uuidValue[16];

    for (size_t i = 5; i < 9; i++)
    {
        success = nfc.mifareultralight_ReadPage(i, userUUID[arrayCounter]);
        if (!success)
        {
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

/**
 * @brief Reads the UUID of an NFC card if present.
 *
 * This function attempts to read the UUID (Unique Identifier) of a card using the NFC controller.
 * It waits for a card to be detected for up to 5000 milliseconds. If a card is found, it returns
 * a pointer to a static array containing the UID. If no card is found within the timeout period,
 * it returns nullptr.
 *
 * @return uint8_t* Pointer to the UID array if a card is found, or nullptr if not found.
 */
uint8_t *NfcController::readCardUUID()
{
    uint8_t found;
    static uint8_t uid[] = {0, 0, 0, 0, 0, 0, 0};
    uint8_t uid_length;
    Serial.println("Reading...");
    found = this->nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uid_length, 5000);
    // Serial.println(found);
    // for (size_t i = 0; i < NTAG216_UID_SIZE; i++)
    // {
    //     Serial.print(uid[i]);
    // }
    if (found)
    {
        return uid;
    }
    else
    {
        return nullptr;
    }
}

/**
 * @brief Converts a UUID represented as a byte array to a formatted string.
 *
 * This function takes a pointer to a UUID byte array and its size, converts each byte to its
 * hexadecimal string representation, and inserts dashes at standard UUID positions (8, 13, 18, 23).
 * If a byte is zero, it is represented as "00".
 *
 * @param uuid Pointer to the UUID byte array.
 * @param size The number of bytes in the UUID array.
 * @return A String containing the formatted UUID.
 */
String NfcController::uuidToString(uint8_t *uuid, uint8_t size)
{
    String finalUuidString;
    uint8_t uuidDashPositions[] = {8, 13, 18, 23};
    uint8_t positionsSize = 4;
    for (size_t i = 0; i < size; i++)
    {
        if (uuid[i] == 0)
        {
            finalUuidString += "00";
        } else if(uuid[i] < 0xf) {
            finalUuidString += "0" + String(uuid[i], HEX);
            //add case when letter A b c itd is leading like 0a 0b 0c 0d
        } 

        else
        {
            finalUuidString += String(uuid[i], HEX);
        }
    }
    for (size_t i = 0; i < positionsSize; i++)
    {
        finalUuidString = insertCharAt(finalUuidString, uuidDashPositions[i], '-');
    }

    return finalUuidString;
}

/**
 * @brief Converts an NFC tag's UID to a hexadecimal string representation.
 *
 * This function takes a pointer to an array of bytes representing the NFC tag's UID
 * and converts it into a single hexadecimal string. Each byte is converted to its
 * hexadecimal representation and concatenated to form the final string.
 *
 * @param nfcUuid Pointer to an array of bytes containing the NFC tag's UID.
 * @return String Hexadecimal string representation of the NFC tag's UID.
 */
String NfcController::nfcTagToString(uint8_t *nfcUuid)
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
    while (i < uuidString.length())
    {
        if (uuidString.substring(i, i + 1) == "-")
            i++;
        String firstPart = uuidString.substring(i, i + 1);
        if (uuidString.substring(i + 1, i + 2) == "-")
            i++;
        String lastPart = uuidString.substring(i + 1, i + 2);
        String finalNumber = firstPart + lastPart;
        Serial.print("Final number: ");
        Serial.print(finalNumber);
        Serial.println();
        finalUUID[j] = stringToHex(finalNumber);
        i += 2;
        j++;
    }
    return finalUUID;
}
