package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.FinancialTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransactions, Long> {
}
