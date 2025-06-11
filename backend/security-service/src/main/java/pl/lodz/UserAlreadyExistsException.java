package pl.lodz.commons;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(final String msg) {
        super(msg);
    }

}
