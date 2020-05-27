package new_emt.demo.repository;

import new_emt.demo.model.ShoppingCart;
import new_emt.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transaction, Long> {
}
