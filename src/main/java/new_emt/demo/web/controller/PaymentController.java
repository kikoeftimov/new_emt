package new_emt.demo.web.controller;

import com.stripe.exception.StripeException;
import new_emt.demo.model.ShoppingCart;
import new_emt.demo.model.dto.ChargeRequest;
import new_emt.demo.model.exceptions.ShoppingCartIsNotActive;
import new_emt.demo.service.AuthService;
import new_emt.demo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Value("${STRIPE_P_KEY}")
    private String publicKey;

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public PaymentController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping("/charge")
    public String getCheckoutPage(Model model){
        ShoppingCart shoppingCart = this.shoppingCartService.findActiveShoppingCartByUsername(this.authService.getCurrentUsername());
        try{
            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("currency", "eur");
            model.addAttribute("amount", shoppingCart.getBooks().size() * 100 * 200);
            model.addAttribute("stripePublicKey", this.publicKey);
            return "checkout";
        }catch (RuntimeException ex) {
            return "redirect:/books?error=" + ex.getLocalizedMessage();
        }
    }


    @PostMapping("/charge")
    public String checkout(ChargeRequest chargeRequest, Model model){
        try {
            ShoppingCart shoppingCart = this.shoppingCartService.checkoutCart(this.authService.getCurrentUsername(), chargeRequest);
            return "redirect:/books?message=Succesfull Payment";
        }catch (RuntimeException ex){
            return "redirect:/payments/charge?error=" + ex.getLocalizedMessage();
        }
    }
}
