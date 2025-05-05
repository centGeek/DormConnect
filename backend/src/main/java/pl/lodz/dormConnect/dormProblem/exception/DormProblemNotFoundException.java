package pl.lodz.dormConnect.dormProblem.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class DormProblemNotFoundException extends WebClientResponseException {
    public DormProblemNotFoundException(String message) {
        super(404, message, null, null, null);
    }

}
