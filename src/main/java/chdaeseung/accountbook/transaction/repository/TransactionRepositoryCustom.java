package chdaeseung.accountbook.transaction.repository;

import chdaeseung.accountbook.transaction.dto.TransactionSearchDto;
import chdaeseung.accountbook.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryCustom {
    Page<Transaction> searchTransactions(Long userId, TransactionSearchDto searchDto, Pageable pageable);
}
