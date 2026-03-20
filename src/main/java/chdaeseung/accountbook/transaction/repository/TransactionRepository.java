package chdaeseung.accountbook.transaction.repository;

import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserOrderByDateDescIdDesc(User user);
}
