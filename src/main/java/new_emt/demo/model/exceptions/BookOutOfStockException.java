package new_emt.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException(String name) {
        super(String.format("Book with name %s is out of stock!", name));
    }
}
