package chdaeseung.accountbook.transaction.dto;

import chdaeseung.accountbook.transaction.entity.ExpenseType;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequestDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long categoryId;

    private TransactionType type;

    private ExpenseType expenseType;

    private Long amount;

    private String memo;
}
