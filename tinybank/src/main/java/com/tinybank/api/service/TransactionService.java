package com.tinybank.api.service;

import com.tinybank.api.domain.Transaction;
import com.tinybank.api.domain.User;
import com.tinybank.api.domain.Wallet;
import com.tinybank.api.repository.TransactionRepository;
import com.tinybank.api.repository.UserRepository;
import com.tinybank.api.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transfer(Long payerId, Long payeeId, BigDecimal amount) {

        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        User payee = userRepository.findById(payeeId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (payer.equals(payee)) {
            throw new IllegalArgumentException("Não pode transferir para si mesmo");
        }

        payer.getWallet().debit(amount);
        payee.getWallet().credit(amount);

        Transaction transaction = Transaction.builder()
                .transactionAmount(amount)
                .sender(payer.getWallet())
                .receiver(payee.getWallet())
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }
}