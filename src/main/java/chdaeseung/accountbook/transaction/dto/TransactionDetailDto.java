package chdaeseung.accountbook.transaction.dto;

import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class TransactionDetailDto {

    private Long id;

    private LocalDate date;

    private String categoryName;

    private TransactionType type;

    private Long amount;

    private String memo;

    public static TransactionDetailDto from(Transaction transaction) {
        return TransactionDetailDto.builder()
                .id(transaction.getId())
                .date(transaction.getDate())
                .categoryName(transaction.getCategory().getName())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .memo(transaction.getMemo())
                .build();
    }
}
