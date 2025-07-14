package com.bancario.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "contaOrigem", "contaDestino" })
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private BigDecimal valor;
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    public Transacao(String tipo, BigDecimal valor, Conta origem, Conta destino) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = LocalDateTime.now();
        this.contaOrigem = origem;
        this.contaDestino = destino;
    }
}
