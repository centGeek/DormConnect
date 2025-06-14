package pl.lodz.dormproblemservice.exception;

public class IllegalProblemStatusChangeException extends RuntimeException {
    public IllegalProblemStatusChangeException(String message) {
        super(message);
    }
}
