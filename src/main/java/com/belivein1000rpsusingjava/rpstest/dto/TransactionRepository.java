package com.belivein1000rpsusingjava.rpstest.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;


public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


    @Query("""
      SELECT COALESCE(SUM(
         CASE WHEN t.type = com.belivein1000rpsusingjava.rpstest.dto.TransactionType.credit
              THEN t.amount
              ELSE -t.amount
         END
      ), 0)
      FROM TransactionEntity t
      WHERE t.account.userId = :userId
    """)
    BigDecimal calculateOperationsBalanceByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM TransactionEntity t WHERE t.account.userId = :userId")
    Long countTransactionsByUserId(@Param("userId") Long userId);

}