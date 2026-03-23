package chdaeseung.accountbook.recurring.dto;

import chdaeseung.accountbook.recurring.entity.RecurringTransaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RecurringTransactionResponseDto {
    private Long id;

    private String memo;

    private Long amount;

    private String categoryName;

    private Integer dayOfMonth;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isDone;

    public static RecurringTransactionResponseDto from(RecurringTransaction recurringTransaction) {
        return RecurringTransactionResponseDto.builder()
                .id(recurringTransaction.getId())
                .memo(recurringTransaction.getMemo())
                .amount(recurringTransaction.getAmount())
                .categoryName(recurringTransaction.getCategory().getName())
                .dayOfMonth(recurringTransaction.getDayOfMonth())
                .startDate(recurringTransaction.getStartDate())
                .endDate(recurringTransaction.getEndDate())
                .isDone(recurringTransaction.isDone())
                .build();
    }
}
