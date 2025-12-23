package com.tinybank.api.controller;

import com.tinybank.api.dto.TransactionDTO;
import com.tinybank.api.repository.TransactionRepository;
import com.tinybank.api.repository.UserRepository;
import com.tinybank.api.repository.WalletRepository;
import com.tinybank.api.service.TransactionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;


    @PostMapping
    @Transactional
    public ResponseEntity createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        transactionService.transfer(transactionDTO.senderId(), transactionDTO.receiverId(), transactionDTO.amount());

        return ResponseEntity.ok().build();

    }
}
