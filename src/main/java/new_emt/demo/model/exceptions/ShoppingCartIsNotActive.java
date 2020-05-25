package new_emt.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class ShoppingCartIsNotActive extends RuntimeException {
    public ShoppingCartIsNotActive(String username) {
        super(String.format("Shopping cart for user %s is not active", username));
    }
}
