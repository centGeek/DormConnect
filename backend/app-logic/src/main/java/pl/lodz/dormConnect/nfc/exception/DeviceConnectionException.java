package pl.lodz.dormConnect.nfc.exception;

public class DeviceConnectionErrorException extends RuntimeException {
    public DeviceConnectionErrorException(String message) {
        super(message);
    }

    public DeviceConnectionErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
