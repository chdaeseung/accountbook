package chdaeseung.accountbook.transaction.dto;

import chdaeseung.accountbook.transaction.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionUpdateDto {
    private TransactionType type;

    private Long amount;

    private Long categoryId;

    private String memo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
