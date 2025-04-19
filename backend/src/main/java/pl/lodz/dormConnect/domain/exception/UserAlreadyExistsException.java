package pl.lodz.dormConnect.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(final String msg) {
        super(msg);
    }

}
