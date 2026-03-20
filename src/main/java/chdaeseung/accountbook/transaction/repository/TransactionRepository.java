package chdaeseung.accountbook.transaction.repository;

import chdaeseung.accountbook.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
