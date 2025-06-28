package com.bancario.demo.controller;

import com.bancario.demo.model.AuthResponse;
import com.bancario.demo.model.Pessoa;
import com.bancario.demo.security.JwtUtil;
import com.bancario.demo.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Pessoa pessoa) {
        try {
            if (pessoa.getPassword() == null || pessoa.getPassword().length() < 8) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A senha deve ter no mínimo 8 caracteres.");
            }
            Pessoa criada = pessoaService.register(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Pessoa pessoa) {
        try {
            Pessoa logada = pessoaService.login(pessoa.getCpf(), pessoa.getPassword());

            String token = jwtUtil.generateToken(logada.getCpf());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarTodos() {
        return ResponseEntity.ok(pessoaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(@PathVariable Long id) {
        return pessoaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return ResponseEntity.ok(pessoaService.atualizar(id, pessoa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pessoaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
