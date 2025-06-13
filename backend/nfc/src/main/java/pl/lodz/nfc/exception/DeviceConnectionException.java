package pl.lodz.nfc.exception;

public class DeviceConnectionException extends RuntimeException {
    public DeviceConnectionException(String message) {
        super(message);
    }

    public DeviceConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
