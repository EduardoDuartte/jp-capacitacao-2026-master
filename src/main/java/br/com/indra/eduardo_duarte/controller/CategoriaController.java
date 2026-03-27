package br.com.indra.eduardo_duarte.controller;

import br.com.indra.eduardo_duarte.model.Categoria;
import br.com.indra.eduardo_duarte.service.CategoriaService;
import br.com.indra.eduardo_duarte.service.dto.CategoriaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.getById(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        try {
            Categoria novaCategoria = categoriaService.criarCategoria(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCategoria(@PathVariable Long id,
                                                @RequestBody CategoriaRequestDTO dto) {
        try {
            Categoria categoriaAtualizada = categoriaService.atualizarCategoria(id, dto);
            return ResponseEntity.ok(categoriaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCategoria(@PathVariable Long id) {
        try {
            categoriaService.deletarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
