package chdaeseung.accountbook.transaction.entity;

import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Long amount;

    private String memo;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transaction(TransactionType type, Long amount, Category category, String memo, LocalDate date, User user) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.memo = memo;
        this.date = date;
        this.user = user;
    }

    public void update(TransactionType type, Long amount, Category category, String memo, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.memo = memo;
        this.date = date;
    }
}
