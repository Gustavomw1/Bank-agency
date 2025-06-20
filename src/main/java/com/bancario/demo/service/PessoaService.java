package com.bancario.demo.service;

import com.bancario.demo.model.Pessoa;
import com.bancario.demo.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public List<Pessoa> listarTodos() {
        return pessoaRepository.findAll();
    }

    public void deletar(Long id) {
        pessoaRepository.deleteById(id);
    }

    public Pessoa atualizar(Long id, Pessoa novaPessoa) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        pessoa.setName(novaPessoa.getName());
        pessoa.setCpf(novaPessoa.getCpf());
        pessoa.setNascimento(novaPessoa.getNascimento());

        return pessoaRepository.save(pessoa);
    }
}
