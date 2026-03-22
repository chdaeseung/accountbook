package chdaeseung.accountbook.transaction.dto;

import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TransactionResponseDto {

    private final Long id;
    private final TransactionType type;
    private final Long amount;
    private final String category;
    private final String memo;
    private final LocalDate date;

    public TransactionResponseDto(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.category = transaction.getCategory().getName();
        this.memo = transaction.getMemo();
        this.date = transaction.getDate();
    }
}
