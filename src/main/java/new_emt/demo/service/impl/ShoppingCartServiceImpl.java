package new_emt.demo.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import new_emt.demo.model.Book;
import new_emt.demo.model.ShoppingCart;
import new_emt.demo.model.Transaction;
import new_emt.demo.model.User;
import new_emt.demo.model.dto.ChargeRequest;
import new_emt.demo.model.enumerations.CartStatus;
import new_emt.demo.model.exceptions.*;
import new_emt.demo.repository.ShoppingCartRepository;
import new_emt.demo.repository.TransactionsRepository;
import new_emt.demo.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final AuthService authService;
    private final BookService bookService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final TransactionsRepository transactionsRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, AuthService authService, BookService bookService, UserService userService, PaymentService paymentService, TransactionsRepository transactionsRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.authService = authService;
        this.bookService = bookService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public ShoppingCart createCart(String username) {
        User user = this.userService.findById(username);
        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(user.getUsername(), CartStatus.CREATED)) {
            throw new ShoppingCartIsAlreadyCreated(username);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }


    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String username) {
        return this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED).
                orElseThrow(() -> new ShoppingCartIsNotActive(username));
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        return this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED).
                orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User user = this.userService.findById(username);
                    shoppingCart.setUser(user);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }


    @Override
    @Transactional
    public ShoppingCart addBookToCart(String username, Long bookId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Book book = this.bookService.findById(bookId);
        for (Book b: shoppingCart.getBooks()){
            if(b.getId().equals(bookId)){
                throw new BookIsAlreadyInShoppingCartException(book.getName());
            }
        }
        if(book.getNumberOfBooks() <= 0){
            throw new BookOutOfStockException(book.getName());
        }
        shoppingCart.getBooks().add(book);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart removeBookFromCart(String username, Long bookId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        shoppingCart.setBooks(shoppingCart.getBooks()
                                          .stream()
                                          .filter(book -> !book.getId().equals(bookId))
                                          .collect(Collectors.toList()));
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart cancelCart(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActive(username));
        shoppingCart.setFinished(LocalDateTime.now());
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutCart(String username, ChargeRequest chargeRequest) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActive(username));

        User user = this.userService.findById(username);

        List<Book> books = shoppingCart.getBooks();
        float price=0;

        for(Book b : books){
            if(b.getNumberOfBooks() <= 0){
                throw new BookOutOfStockException(b.getName());
            }
            b.setNumberOfBooks(b.getNumberOfBooks() - 1);
            price+=200;
        }

        Charge charge = null;
        try {
            charge = this.paymentService.charge(chargeRequest);
        }catch (StripeException e) {
            throw new TransactionFailedException(username, e.getMessage());
        }
        Transaction transaction = new Transaction();
        transaction.setShoppingCart(shoppingCart);
        transaction.setAmount(chargeRequest.getAmount()/100);
        transaction.setUser(user);
        transaction.setStatus(charge.getStatus());
        transaction.setLocalDateTime(LocalDateTime.now());
        transaction.setPaymentMethod(charge.getPaymentMethod());
        transaction.setRefunded(charge.getRefunded());
        transaction.setEmail(charge.getBillingDetails().getName());
        this.transactionsRepository.save(transaction);

        shoppingCart.setBooks(books);
        shoppingCart.setFinished(LocalDateTime.now());
        shoppingCart.setStatus(CartStatus.FINISHED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart clearCart(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActive(username));

        shoppingCart.setBooks(new ArrayList<>());
        return this.shoppingCartRepository.save(shoppingCart);
    }
}
