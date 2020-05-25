package new_emt.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class BookIsAlreadyInShoppingCartException extends RuntimeException{
    public BookIsAlreadyInShoppingCartException(String bookName) {

        super(String.format("Book with name %s is already in a shopping cart", bookName));
    }


}
