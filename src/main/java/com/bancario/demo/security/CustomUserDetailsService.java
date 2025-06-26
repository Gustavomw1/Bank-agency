package com.bancario.demo.security;

import com.bancario.demo.model.Pessoa;
import com.bancario.demo.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Pessoa pessoa = pessoaRepository.findByCpf(cpf);
        if (pessoa == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return new User(pessoa.getCpf(), pessoa.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}