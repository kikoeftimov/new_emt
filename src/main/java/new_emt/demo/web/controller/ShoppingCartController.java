package new_emt.demo.web.controller;

import new_emt.demo.model.ShoppingCart;
import new_emt.demo.service.AuthService;
import new_emt.demo.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }


    @PostMapping("/{bookId}/add-book")
    public String addProductToCart(@PathVariable Long bookId){
        try{
            ShoppingCart shoppingCart = this.shoppingCartService.addBookToCart(this.authService.getCurrentUsername(), bookId);
        }catch (RuntimeException ex){
            return "redirect:/books?error=" + ex.getLocalizedMessage();
        }
        return "redirect:/books";
    }


    @PostMapping("/{bookId}/remove-book")
    public String removeBookFromCart(@PathVariable Long bookId){
        ShoppingCart shoppingCart = this.shoppingCartService.removeBookFromCart(this.authService.getCurrentUsername(), bookId);
        return "redirect:/books";
    }
}
