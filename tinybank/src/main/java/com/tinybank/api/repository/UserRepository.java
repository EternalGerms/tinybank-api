package com.tinybank.api.repository;

import com.tinybank.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByCpf(String cpf);
}
