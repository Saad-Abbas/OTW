package com.example.otwAppservice.service.financialTransactionService;

import com.example.otwAppservice.entity.FinancialTransactions;
import com.example.otwAppservice.repository.FeedbackHistoryRepository;
import com.example.otwAppservice.repository.FinancialTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;


    @Override
    public FinancialTransactions saveFinancialTransaction(FinancialTransactions financialTransactions) {
        return financialTransactionRepository.save(financialTransactions);
    }
}
