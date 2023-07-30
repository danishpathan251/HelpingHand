package com.donate.dao;

import com.donate.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{

    List<Transaction> findAllByUserId(Long userId);
}