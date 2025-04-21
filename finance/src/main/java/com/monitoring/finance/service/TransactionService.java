package com.monitoring.finance.service;

import com.monitoring.finance.model.Transaction;
import com.monitoring.finance.enums.Currencies;
import com.monitoring.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<Currencies, Double> sumAmountsByCurrency() {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction != null && transaction.getCurrency() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }


    public Map<Currencies, Long> countTransactionsByCurrency() {
        return transactionRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Transaction::getCurrency, Collectors.counting()));
    }

    public Transaction save(Transaction transaction) {
        transaction.setValue(convertToRSD(transaction.getAmount(), transaction.getCurrency()));
        return transactionRepository.save(transaction);
    }

    private double convertToRSD(double amount, Currencies currency) {
        switch (currency) {
            case RSD: return amount;
            case USD: return amount * 112.0;
            case EUR: return amount * 116.0;
            case BTC: return amount * 7000000.0;
            case ETH: return amount * 350000.0;
            case SOL: return amount * 14000.0;
            default: return amount;
        }
    }


    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
