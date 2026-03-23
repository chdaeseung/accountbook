package chdaeseung.accountbook.transaction.dto;

import chdaeseung.accountbook.transaction.entity.ExpenseType;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class TransactionResponseDto {
    private final Long id;

    private final TransactionType type;

    private final ExpenseType expenseType;

    private final Long amount;

    private final String category;

    private final String memo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    public TransactionResponseDto(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.expenseType = transaction.getExpenseType();
        this.amount = transaction.getAmount();
        this.category = transaction.getCategory().getName();
        this.memo = transaction.getMemo();
        this.date = transaction.getDate();
    }
}
