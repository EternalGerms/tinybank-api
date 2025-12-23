package com.tinybank.api.config;

import com.tinybank.api.domain.User;
import com.tinybank.api.domain.Wallet;
import com.tinybank.api.repository.UserRepository;
import com.tinybank.api.service.TransactionService;
import jakarta.transaction.Transactional; // <--- Importante
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner run() {
        return new CommandLineRunner() {

            @Override
            @Transactional
            public void run(String... args) throws Exception {
                System.out.println("============== INICIANDO O SMOKE TEST ==============");

                userRepository.deleteAll();

                // 1. Cria João
                User joao = new User();
                joao.setCpf("111.111.111-11");
                joao.setEmail("joao@email.com");

                Wallet carteiraJoao = new Wallet();
                carteiraJoao.setBalance(new BigDecimal("100.00"));
                carteiraJoao.setUser(joao);
                joao.setWallet(carteiraJoao);

                userRepository.save(joao);
                System.out.println("✅ João criado com saldo: " + joao.getWallet().getBalance());

                // 2. Cria Maria
                User maria = new User();
                maria.setCpf("222.222.222-22");
                maria.setEmail("maria@email.com");

                Wallet carteiraMaria = new Wallet();
                carteiraMaria.setBalance(new BigDecimal("0.00"));
                carteiraMaria.setUser(maria);
                maria.setWallet(carteiraMaria);

                userRepository.save(maria);
                System.out.println("✅ Maria criada com saldo: " + maria.getWallet().getBalance());

                // 3. Transfere
                System.out.println("💸 Transferindo R$ 40.00 do João para Maria...");
                transactionService.transfer(joao.getWallet().getId(), maria.getWallet().getId(), new BigDecimal("40.00"));

                // 4. Valida
                User joaoFinal = userRepository.findById(joao.getId()).get();
                User mariaFinal = userRepository.findById(maria.getId()).get();

                System.out.println("============== RESULTADO FINAL ==============");
                System.out.println("👤 João (Esperado: 60.00) -> Atual: " + joaoFinal.getWallet().getBalance());
                System.out.println("👤 Maria (Esperado: 40.00) -> Atual: " + mariaFinal.getWallet().getBalance());
                System.out.println("=============================================");
            }
        };
    }
}