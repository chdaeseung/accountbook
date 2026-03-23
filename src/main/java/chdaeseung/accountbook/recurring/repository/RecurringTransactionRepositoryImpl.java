package chdaeseung.accountbook.recurring.repository;

import chdaeseung.accountbook.category.entity.QCategory;
import chdaeseung.accountbook.recurring.entity.QRecurringTransaction;
import chdaeseung.accountbook.recurring.entity.RecurringTransaction;
import chdaeseung.accountbook.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecurringTransactionRepositoryImpl implements RecurringTransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecurringTransaction> findAllToGenerate(LocalDate today) {
        QRecurringTransaction recurringTransaction = QRecurringTransaction.recurringTransaction;
        QCategory category = QCategory.category;
        QUser user = QUser.user;

        return queryFactory
                .selectFrom(recurringTransaction)
                .join(recurringTransaction.category, category).fetchJoin()
                .join(recurringTransaction.user, user).fetchJoin()
                .where(
                        recurringTransaction.isDone.isTrue(),
                        recurringTransaction.dayOfMonth.eq(today.getDayOfMonth()),
                        recurringTransaction.startDate.loe(today),
                        recurringTransaction.endDate.isNull()
                                .or(recurringTransaction.endDate.goe(today))
                )
                .fetch();
    }
}
