package new_emt.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class PasswordDoesntMatchException extends RuntimeException {
    public PasswordDoesntMatchException(){
        super("Password doesn't match!!");
    }
}
