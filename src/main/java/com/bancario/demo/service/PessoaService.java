package com.bancario.demo.service;

import com.bancario.demo.model.Conta;
import com.bancario.demo.model.Pessoa;
import com.bancario.demo.repository.ContaRepository;
import com.bancario.demo.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Pessoa register(Pessoa pessoa) {
        pessoa.setPassword(encoder.encode(pessoa.getPassword()));
        Pessoa criada = pessoaRepository.save(pessoa);

        contaRepository.save(new Conta(criada));

        return criada;
    }

    public Pessoa login(String cpf, String rawPassword) {
        Pessoa pessoa = pessoaRepository.findByCpf(cpf);
        if (pessoa == null || !encoder.matches(rawPassword, pessoa.getPassword())) {
            throw new RuntimeException("CPF ou senha inválidos");
        }
        return pessoa;
    }

    public List<Pessoa> listarTodos() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public Pessoa atualizar(Long id, Pessoa novaPessoa) {
        Pessoa atual = pessoaRepository.findById(id).orElseThrow();
        atual.setNome(novaPessoa.getNome());
        atual.setNascimento(novaPessoa.getNascimento());
        atual.setPassword(encoder.encode(novaPessoa.getPassword()));
        return pessoaRepository.save(atual);
    }

    public void deletar(Long id) {
        pessoaRepository.deleteById(id);
    }
}
