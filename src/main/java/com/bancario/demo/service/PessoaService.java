package com.bancario.demo.service;

import com.bancario.demo.model.Pessoa;
import com.bancario.demo.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa register(Pessoa pessoa) {
        Optional<Pessoa> existente = pessoaRepository.findByCpf(pessoa.getCpf());
        if (existente.isPresent()) {
            throw new RuntimeException("CPF já está em uso.");
        }
        return pessoaRepository.save(pessoa);
    }

    public Pessoa login(String cpf, String password) {
        Pessoa pessoa = pessoaRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("CPF não encontrado"));

        if (!pessoa.getPassword().equals(password)) {
            throw new RuntimeException("Senha inválida.");
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
        return pessoaRepository.findById(id).map(p -> {
            p.setNome(novaPessoa.getNome());
            p.setCpf(novaPessoa.getCpf());
            p.setNascimento(novaPessoa.getNascimento());
            p.setPassword(novaPessoa.getPassword());
            return pessoaRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));
    }

    public void deletar(Long id) {
        pessoaRepository.deleteById(id);
    }
}
