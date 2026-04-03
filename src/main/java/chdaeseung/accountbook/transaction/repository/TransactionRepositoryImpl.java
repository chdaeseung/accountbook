package chdaeseung.accountbook.transaction.repository;

import chdaeseung.accountbook.category.entity.QCategory;
import chdaeseung.accountbook.transaction.dto.TransactionSearchDto;
import chdaeseung.accountbook.transaction.entity.ExpenseType;
import chdaeseung.accountbook.transaction.entity.QTransaction;
import chdaeseung.accountbook.transaction.entity.Transaction;
import chdaeseung.accountbook.transaction.entity.TransactionType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static chdaeseung.accountbook.transaction.entity.QTransaction.transaction;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Transaction> searchTransactions(Long userId, TransactionSearchDto searchDto, Pageable pageable) {
        QTransaction transaction = QTransaction.transaction;
        QCategory category = QCategory.category;

        List<Transaction> content = queryFactory
                .selectFrom(transaction)
                .join(transaction.category, category).fetchJoin()
                .where(
                        userIdEq(userId, transaction),
                        startDateGoe(searchDto.getStartDate(), transaction),
                        endDateLoe(searchDto.getEndDate(), transaction),
                        categoryIdEq(searchDto.getCategoryId(), category),
                        typeEq(searchDto.getType(), transaction),
                        expenseTypeEq(searchDto.getExpenseType(), transaction),
                        eqBankAccountId(searchDto.getBankAccountId())
                )
                .orderBy(transaction.date.desc(), transaction.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(transaction.count())
                .from(transaction)
                .join(transaction.category, category)
                .where(
                        userIdEq(userId, transaction),
                        startDateGoe(searchDto.getStartDate(), transaction),
                        endDateLoe(searchDto.getEndDate(), transaction),
                        categoryIdEq(searchDto.getCategoryId(), category),
                        typeEq(searchDto.getType(), transaction),
                        expenseTypeEq(searchDto.getExpenseType(), transaction),
                        eqBankAccountId(searchDto.getBankAccountId())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }

    private BooleanExpression userIdEq(Long userId, QTransaction transaction) {
        return userId != null ? transaction.user.id.eq(userId) : null;
    }

    private BooleanExpression startDateGoe(LocalDate startDate, QTransaction transaction) {
        return startDate != null ? transaction.date.goe(startDate) : null;
    }

    private BooleanExpression endDateLoe(LocalDate endDate, QTransaction transaction) {
        return endDate != null ? transaction.date.loe(endDate) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId, QCategory category) {
        return categoryId != null ? category.id.eq(categoryId) : null;
    }

    private BooleanExpression typeEq(TransactionType type, QTransaction transaction) {
        return type != null ? transaction.type.eq(type) : null;
    }

    private BooleanExpression expenseTypeEq(ExpenseType expenseType, QTransaction transaction) {
        return expenseType != null ? transaction.expenseType.eq(expenseType) : null;
    }

    private BooleanExpression eqBankAccountId(Long bankAccountId) {
        return bankAccountId != null ? transaction.bankAccount.id.eq(bankAccountId) : null;
    }
}
