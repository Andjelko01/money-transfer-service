package com.andjelko.moneytransferservice.datasource.entities;

import com.andjelko.moneytransferservice.service.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions",
        indexes = {
                @Index(name = "source_account_id_index", columnList = "source_account_id"),
                @Index(name = "destination_account_id_index", columnList = "destination_account_id"),
        })
public class TransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sourceAccountId;

    @Column(nullable = false)
    private Long destinationAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 1000)
    private String message;

}
