package com.bancario.demo.controller;

import com.bancario.demo.model.Conta;
import com.bancario.demo.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/{idPessoa}/criar")
    public ResponseEntity<?> criarConta(@PathVariable Long idPessoa) {
        try {
            Conta conta = contaService.criarContaParaPessoa(idPessoa);
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idPessoa}/saldo")
    public ResponseEntity<?> saldo(@PathVariable Long idPessoa) {
        try {
            return ResponseEntity.ok(contaService.consultarSaldo(idPessoa));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{idPessoa}/depositar")
    public ResponseEntity<?> depositar(@PathVariable Long idPessoa, @RequestParam BigDecimal valor) {
        try {
            contaService.depositar(idPessoa, valor);
            return ResponseEntity.ok("Depósito realizado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{idPessoa}/sacar")
    public ResponseEntity<?> sacar(@PathVariable Long idPessoa, @RequestParam BigDecimal valor) {
        try {
            contaService.sacar(idPessoa, valor);
            return ResponseEntity.ok("Saque realizado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{idPessoa}/transferir")
    public ResponseEntity<?> transferir(
            @PathVariable Long idPessoa,
            @RequestParam Long destinoId,
            @RequestParam BigDecimal valor) {
        try {
            contaService.transferir(idPessoa, destinoId, valor);
            return ResponseEntity.ok("Transferência realizada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idPessoa}/transacoes")
    public ResponseEntity<?> historico(@PathVariable Long idPessoa) {
        try {
            return ResponseEntity.ok(contaService.listarTransacoes(idPessoa));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
