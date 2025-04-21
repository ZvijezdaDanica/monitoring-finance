package com.monitoring.finance.controller;

import com.monitoring.finance.enums.Currencies;
import com.monitoring.finance.enums.TransactionTypes;
import com.monitoring.finance.model.Transaction;
import com.monitoring.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/pie")
    public String showAmountsByCurrency(Model model) {
        Map<Currencies, Double> currencyAmounts = transactionService.sumAmountsByCurrency();
        model.addAttribute("currencyAmounts", currencyAmounts);
        return "transactions/pie"; // Thymeleaf template name
    }

    @GetMapping("pie/count")
    public String showPieChart(Model model) {
        Map<Currencies, Long> currencyCounts = transactionService.countTransactionsByCurrency();
        model.addAttribute("currencyCounts", currencyCounts);
        return "transactions/pie";
    }

    @GetMapping
    public String listTransactions(Model model) {
        List<Transaction> transactions = transactionService.findAll();
        model.addAttribute("transactions", transactions);

        // Total value of all transactions (sum of value)
        Double totalValue = transactions.stream()
                .filter(Objects::nonNull)
                .map(Transaction::getValue)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
        model.addAttribute("totalValue", totalValue);

        // Current total value (subtract SPEND values)
        double totalCurrentValue = transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getValue() != null && t.getType() != null)
                .mapToDouble(t -> t.getType() == TransactionTypes.SPEND ? -t.getValue() : t.getValue())
                .sum();
        model.addAttribute("totalCurrentValue", totalCurrentValue);

        // Value by currency (sum of value per currency)
        Map<Currencies, Double> valueByCurrency = transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getValue() != null && t.getCurrency() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        Collectors.summingDouble(Transaction::getValue)
                ));
        model.addAttribute("valueByCurrency", valueByCurrency);

        // Current value by currency (subtract SPEND values per currency)
        Map<Currencies, Double> currentValueByCurrency = transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getValue() != null && t.getCurrency() != null && t.getType() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        Collectors.summingDouble(t -> t.getType() == TransactionTypes.SPEND ? -t.getValue() : t.getValue())
                ));
        model.addAttribute("currentValueByCurrency", currentValueByCurrency);

        // Amount by currency (sum of amount per currency)
        Map<Currencies, Double> amountByCurrency = transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getAmount() != null && t.getCurrency() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
        model.addAttribute("amountByCurrency", amountByCurrency);

        // Current amount by currency (subtract SPEND amounts per currency)
        Map<Currencies, Double> currentAmountByCurrency = transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getAmount() != null && t.getCurrency() != null && t.getType() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCurrency,
                        Collectors.summingDouble(t -> t.getType() == TransactionTypes.SPEND ? -t.getAmount() : t.getAmount())
                ));
        model.addAttribute("currentAmountByCurrency", currentAmountByCurrency);

        // For backward compatibility or other uses
        model.addAttribute("currencyAmounts", valueByCurrency);

        return "transactions/list";
    }

    // Show form to create new transaction
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transactions/form";
    }

    // Handle form submission for creating or updating a transaction
    @PostMapping
    public String saveTransaction(@Valid @ModelAttribute Transaction transaction, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Return to form with validation errors displayed
            return "transactions/form";
        }
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        transactionService.save(transaction);
        return "redirect:/transactions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Transaction> transactionOpt = transactionService.findById(id);
        if (transactionOpt.isEmpty()) {
            return "redirect:/transactions";
        }
        model.addAttribute("transaction", transactionOpt.get());
        return "transactions/form";
    }

    // Delete a transaction
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return "redirect:/transactions";
    }
}
