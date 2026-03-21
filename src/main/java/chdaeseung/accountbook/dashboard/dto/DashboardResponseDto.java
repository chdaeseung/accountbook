package chdaeseung.accountbook.dashboard.dto;

import chdaeseung.accountbook.transaction.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponseDto {

    private Long totalIncome;
    private Long totalExpense;
    private long balance;
    private List<ResponseDto> recentTransactions;
}
