package chdaeseung.accountbook.recurring.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecurringTransaction is a Querydsl query type for RecurringTransaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecurringTransaction extends EntityPathBase<RecurringTransaction> {

    private static final long serialVersionUID = -1311848530L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecurringTransaction recurringTransaction = new QRecurringTransaction("recurringTransaction");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final chdaeseung.accountbook.category.entity.QCategory category;

    public final NumberPath<Integer> dayOfMonth = createNumber("dayOfMonth", Integer.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDone = createBoolean("isDone");

    public final StringPath memo = createString("memo");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final chdaeseung.accountbook.user.entity.QUser user;

    public QRecurringTransaction(String variable) {
        this(RecurringTransaction.class, forVariable(variable), INITS);
    }

    public QRecurringTransaction(Path<? extends RecurringTransaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecurringTransaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecurringTransaction(PathMetadata metadata, PathInits inits) {
        this(RecurringTransaction.class, metadata, inits);
    }

    public QRecurringTransaction(Class<? extends RecurringTransaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new chdaeseung.accountbook.category.entity.QCategory(forProperty("category"), inits.get("category")) : null;
        this.user = inits.isInitialized("user") ? new chdaeseung.accountbook.user.entity.QUser(forProperty("user")) : null;
    }

}

