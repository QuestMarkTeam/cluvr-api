package com.example.cluvrapi.domain.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentPending is a Querydsl query type for PaymentPending
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentPending extends EntityPathBase<PaymentPending> {

    private static final long serialVersionUID = 1036293813L;

    public static final QPaymentPending paymentPending = new QPaymentPending("paymentPending");

    public final BooleanPath committed = createBoolean("committed");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiresAt = createDateTime("expiresAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> finalAmount = createNumber("finalAmount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath orderId = createString("orderId");

    public final StringPath orderName = createString("orderName");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath uuid = createString("uuid");

    public QPaymentPending(String variable) {
        super(PaymentPending.class, forVariable(variable));
    }

    public QPaymentPending(Path<? extends PaymentPending> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentPending(PathMetadata metadata) {
        super(PaymentPending.class, metadata);
    }

}

