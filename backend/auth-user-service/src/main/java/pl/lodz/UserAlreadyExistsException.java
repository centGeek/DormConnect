package pl.lodz;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(final String msg) {
        super(msg);
    }

}
