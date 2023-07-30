package com.donate.services;

import com.donate.dao.TransactionRepository;
import com.donate.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }
}
