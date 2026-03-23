package chdaeseung.accountbook.recurring.dto;

import chdaeseung.accountbook.transaction.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RecurringTransactionCreateDto {
    private String memo;

    private Long amount;

    private Integer dayOfMonth;

    private Long categoryId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean isDone;
}
