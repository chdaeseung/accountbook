package chdaeseung.accountbook.dashboard.service;

import chdaeseung.accountbook.dashboard.dto.DashboardResponseDto;
import chdaeseung.accountbook.transaction.dto.TransactionResponseDto;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import chdaeseung.accountbook.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardResponseDto getDashboard(Long userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        long totalIncome = 0L;
        long totalExpense = 0L;

        for(Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.INCOME) {
                totalIncome += transaction.getAmount();
            } else if (transaction.getType() == TransactionType.EXPENSE) {
                totalExpense += transaction.getAmount();
            }
        }

        long balance = totalIncome - totalExpense;

        List<TransactionResponseDto> recentTransactions = transactionRepository
                .findTop5ByUserIdOrderByDateDescIdDesc(userId)
                .stream()
                .map(TransactionResponseDto::new)
                .toList();

        return new DashboardResponseDto(totalIncome, totalExpense, balance, recentTransactions);
    }
}
