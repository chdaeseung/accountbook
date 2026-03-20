package chdaeseung.accountbook.transaction.dto;

import chdaeseung.accountbook.transaction.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateDto {
    @NotNull(message = "거래 유형을 선택해주세요.")
    private TransactionType type;

    @NotNull(message = "금액을 입력해주세요.")
    private Long amount;

    @NotBlank(message = "카테고리를 입력해주세요.")
    private String category;

    private String memo;

    @NotNull(message = "날짜를 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
