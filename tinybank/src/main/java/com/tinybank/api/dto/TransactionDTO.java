package com.tinybank.api.dto;

import com.tinybank.api.domain.Wallet;


import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record TransactionDTO(Long senderId, Long receiverId, @NotNull @Positive BigDecimal amount) {

}
