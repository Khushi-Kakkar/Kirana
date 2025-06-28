package com.kirana.register.repository;

import com.kirana.register.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
