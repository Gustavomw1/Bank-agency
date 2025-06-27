package com.bancario.demo.repository;

import com.bancario.demo.model.Conta;
import com.bancario.demo.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByPessoa(Pessoa pessoa);
}
