package com.belivein1000rpsusingjava.rpstest.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    // Связь с Account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Account account;

    // Сумма транзакции, например,  DECIMAL(18,2)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    // Тип транзакции: debit или credit
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    // Связанная транзакция (например, для отмены/связанного перевода), может быть null
    @Column(name = "reference_id")
    private Long referenceId;

    // Статус транзакции
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt;

    // Удобный конструктор для создания транзакции без временных меток
    public TransactionEntity(Account account, BigDecimal amount, TransactionType type, Long referenceId, TransactionStatus status) {
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.referenceId = referenceId;
        this.status = status;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }
}