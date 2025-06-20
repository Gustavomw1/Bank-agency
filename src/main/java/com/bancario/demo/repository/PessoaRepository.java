package com.bancario.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancario.demo.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
