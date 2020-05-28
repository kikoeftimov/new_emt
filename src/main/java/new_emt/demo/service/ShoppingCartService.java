package new_emt.demo.service;

import new_emt.demo.model.ShoppingCart;
import new_emt.demo.model.dto.ChargeRequest;

public interface ShoppingCartService{

    ShoppingCart findActiveShoppingCartByUsername(String username);

    ShoppingCart getActiveShoppingCart(String username);

    ShoppingCart createCart(String username);

    ShoppingCart addBookToCart(String username, Long bookId);

    ShoppingCart removeBookFromCart(String username, Long bookId);

    ShoppingCart cancelCart(String username);

    ShoppingCart checkoutCart(String username, ChargeRequest chargeRequest);

    ShoppingCart clearCart(String username);
}
