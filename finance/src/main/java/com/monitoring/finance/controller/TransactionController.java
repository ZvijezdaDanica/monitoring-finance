package com.monitoring.finance.controller;

import com.monitoring.finance.model.Transaction;
import com.monitoring.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // List all transactions
    @GetMapping
    public String listTransactions(Model model) {
        model.addAttribute("transactions", transactionService.findAll());
        return "transactions/list";  // src/main/resources/templates/transactions/list.html
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

    // Delete a transaction
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return "redirect:/transactions";
    }
}
