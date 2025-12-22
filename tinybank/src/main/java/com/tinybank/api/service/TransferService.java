package com.tinybank.api.service;

import com.tinybank.api.domain.Transaction;
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
public class TransferService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transfer(Long payerId, Long payeeId, BigDecimal amount) {


        Wallet payer = walletRepository.findById(payerId)
                .orElseThrow(() -> new RuntimeException("Pagador não encontrado"));

        Wallet payee = walletRepository.findById(payeeId)
                .orElseThrow(() -> new RuntimeException("Recebedor não encontrado"));

        if (payer.equals(payee)) {
            throw new IllegalArgumentException("Não pode transferir para si mesmo");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (payer.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        payer.setBalance(payer.getBalance().subtract(amount));
        payee.setBalance(payee.getBalance().add(amount));

        walletRepository.save(payer);
        walletRepository.save(payee);


        Transaction transaction = Transaction.builder()
                .transactionAmount(amount)
                .sender(payer)
                .receiver(payee)
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }
}