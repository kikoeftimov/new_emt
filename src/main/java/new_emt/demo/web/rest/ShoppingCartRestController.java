package new_emt.demo.web.rest;

import new_emt.demo.model.ShoppingCart;
import new_emt.demo.service.AuthService;
import new_emt.demo.service.BookService;
import new_emt.demo.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartRestController {

    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;
    private final AuthService authService;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService, BookService bookService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.bookService = bookService;
        this.authService = authService;
    }

    @PostMapping
    public ShoppingCart createCart(){
        return this.shoppingCartService.createCart(this.authService.getCurrentUsername());
    }

    @PatchMapping("/{bookId}/books")
    public ShoppingCart addBooksToCart(@PathVariable Long bookId){
        return this.shoppingCartService.addBookToCart(
                this.authService.getCurrentUsername(), bookId);
    }

    @DeleteMapping("/{bookId}/books")
    public ShoppingCart removeBooksFromCart(@PathVariable Long bookId){
        return this.shoppingCartService.removeBookFromCart(
                this.authService.getCurrentUsername(), bookId);
    }

    @PatchMapping("/cancel")
    public ShoppingCart cancelCart(){
        return this.shoppingCartService.cancelCart(this.authService.getCurrentUsername());
    }

//    @PatchMapping("/checkout")
//    public ShoppingCart checkoutCart(){
//        return this.shoppingCartService.checkoutCart(this.authService.getCurrentUsername());
//    }
}
