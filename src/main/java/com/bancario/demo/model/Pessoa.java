package com.bancario.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data // Gera getters,setters, toString, hashCode e equals
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera todos os atributos como parâmetros
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cpf;

    private String password;

    private String nascimento;
}
