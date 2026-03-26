package chdaeseung.accountbook.bank.entity;

import chdaeseung.accountbook.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;

    private String accountNumber;

    private Long balance;

    @Enumerated(EnumType.STRING)
    private BankAccountType type;

    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BankAccount(String bankName, String accountNumber, Long balance, BankAccountType type, boolean used, User user) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
        this.used = used;
        this.user = user;
    }

    public void update(String bankName, String accountNumber, Long balance, BankAccountType type, boolean used) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
        this.used = used;
    }
}
