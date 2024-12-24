package com.example.Bank1.repository;

import java.util.List;
import com.example.Bank1.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceIdOrTargetId(Long sourceId, Long targetId);
    List<Transaction> findBySourceIdInOrTargetIdIn(List<Long> sourceIds, List<Long> targetIds);
}
