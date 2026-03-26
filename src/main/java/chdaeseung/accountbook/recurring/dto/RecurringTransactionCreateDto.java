package chdaeseung.accountbook.recurring.dto;

import chdaeseung.accountbook.transaction.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RecurringTransactionCreateDto {
    private String memo;

    @NotNull(message = "금액을 입력해주세요.")
    private Long amount;

    @NotNull(message = "결제일을 입력해주세요.")
    private Integer dayOfMonth;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    @NotNull(message = "정기 결제 시작일을 선택해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "정기 결제 종료일을 선택해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean isDone;
}
