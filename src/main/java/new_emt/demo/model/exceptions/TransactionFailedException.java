package new_emt.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class TransactionFailedException extends RuntimeException {
    public TransactionFailedException(String username, String message) {
        super(String.format("Transaction for user %s failed! Message: %s", username, message));
    }
}
