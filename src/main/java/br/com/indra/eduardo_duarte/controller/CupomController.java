package br.com.indra.eduardo_duarte.controller;

import br.com.indra.eduardo_duarte.model.Cupom;
import br.com.indra.eduardo_duarte.service.CupomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cupons")
@RequiredArgsConstructor
public class CupomController {

    private final CupomService cupomService;

    @GetMapping
    public ResponseEntity<List<Cupom>> listarCupons() {
        return ResponseEntity.ok(cupomService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCupomPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cupomService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarCupomPorCodigo(@PathVariable String codigo) {
        try {
            return ResponseEntity.ok(cupomService.getByCodigo(codigo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarCupom(@RequestBody Cupom cupom) {
        try {
            Cupom novoCupom = cupomService.criarCupom(cupom);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCupom);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCupom(@PathVariable Long id,
                                            @RequestBody Cupom cupom) {
        try {
            Cupom cupomAtualizado = cupomService.atualizarCupom(id, cupom);
            return ResponseEntity.ok(cupomAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCupom(@PathVariable Long id) {
        try {
            cupomService.deletarCupom(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}