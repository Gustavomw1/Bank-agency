package com.bancario.demo.service;

import com.bancario.demo.model.Conta;
import com.bancario.demo.model.Pessoa;
import com.bancario.demo.model.Transacao;
import com.bancario.demo.repository.ContaRepository;
import com.bancario.demo.repository.PessoaRepository;
import com.bancario.demo.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Conta criarContaParaPessoa(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        if (contaRepository.findByPessoa(pessoa).isPresent()) {
            throw new RuntimeException("Pessoa já possui conta");
        }

        Conta conta = new Conta(pessoa);
        return contaRepository.save(conta);
    }

    public BigDecimal consultarSaldo(Long pessoaId) {
        Conta conta = buscarContaPorPessoaId(pessoaId);
        return conta.getSaldo();
    }

    public void depositar(Long pessoaId, BigDecimal valor) {
        Conta conta = buscarContaPorPessoaId(pessoaId);
        conta.depositar(valor);
        contaRepository.save(conta);

        transacaoRepository.save(new Transacao("DEPOSITO", valor, conta, null));
    }

    public void sacar(Long pessoaId, BigDecimal valor) {
        Conta conta = buscarContaPorPessoaId(pessoaId);
        if (!conta.sacar(valor)) {
            throw new RuntimeException("Saldo insuficiente");
        }
        contaRepository.save(conta);
        transacaoRepository.save(new Transacao("SAQUE", valor, conta, null));
    }

    public void transferir(Long origemPessoaId, Long destinoPessoaId, BigDecimal valor) {
        Conta origem = buscarContaPorPessoaId(origemPessoaId);
        Conta destino = buscarContaPorPessoaId(destinoPessoaId);

        if (!origem.sacar(valor)) {
            throw new RuntimeException("Saldo insuficiente para transferência");
        }

        destino.depositar(valor);
        contaRepository.save(origem);
        contaRepository.save(destino);

        transacaoRepository.save(new Transacao("TRANSFERENCIA", valor, origem, destino));
    }

    public List<Transacao> listarTransacoes(Long pessoaId) {
        Conta conta = buscarContaPorPessoaId(pessoaId);
        return transacaoRepository.findByContaOrigemOrContaDestino(conta, conta);
    }

    private Conta buscarContaPorPessoaId(Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        return contaRepository.findByPessoa(pessoa)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada para esta pessoa"));
    }
}
