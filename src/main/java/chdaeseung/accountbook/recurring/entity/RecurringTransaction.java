package chdaeseung.accountbook.recurring.entity;

import chdaeseung.accountbook.category.entity.Category;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import chdaeseung.accountbook.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RecurringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memo;

    private Long amount;

    private Integer dayOfMonth;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public RecurringTransaction(String memo, Long amount, Integer dayOfMonth, LocalDate startDate, LocalDate endDate, boolean isDone, Category category, User user) {
        this.memo = memo;
        this.amount = amount;
        this.dayOfMonth = dayOfMonth;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDone = isDone;
        this.category = category;
        this.user = user;
    }

    public void update(String memo, Long amount, Integer dayOfMonth, LocalDate startDate, LocalDate endDate, boolean isDone, Category category) {
        this.memo = memo;
        this.amount = amount;
        this.dayOfMonth = dayOfMonth;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDone = isDone;
        this.category = category;
    }

    public void deactivate() {
        this.isDone = false;
    }

    public void activate() {
        this.isDone = true;
    }
}

