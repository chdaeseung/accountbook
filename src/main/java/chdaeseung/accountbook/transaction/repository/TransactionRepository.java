package chdaeseung.accountbook.transaction.repository;

import chdaeseung.accountbook.transaction.dto.ResponseDto;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserOrderByDateDescIdDesc(User user);

    Optional<Transaction> findById(Long id);

    boolean existsByCategoryId(Long categoryId);
    
    List<Transaction> findTop5ByUserIdOrderByDateDescIdDesc(Long userId);

    List<Transaction> findAllByUserId(Long userId);
}
