package com.bancario.demo.repository;

import com.bancario.demo.model.Transacao;
import com.bancario.demo.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByContaOrigemOrContaDestino(Conta origem, Conta destino);
}
