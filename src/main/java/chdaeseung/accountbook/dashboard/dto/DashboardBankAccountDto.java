package chdaeseung.accountbook.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardBankAccountDto {
    private Long id;

    private String bankName;

    private String accountNumber;

    private Long balance;

    private String typeLabel;

    private boolean used;
}
