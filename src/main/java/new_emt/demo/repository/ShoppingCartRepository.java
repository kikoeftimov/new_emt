package new_emt.demo.repository;

import new_emt.demo.model.ShoppingCart;
import new_emt.demo.model.enumerations.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserUsernameAndStatus(String username, CartStatus status);
    boolean existsByUserUsernameAndStatus(String username, CartStatus status);
}
