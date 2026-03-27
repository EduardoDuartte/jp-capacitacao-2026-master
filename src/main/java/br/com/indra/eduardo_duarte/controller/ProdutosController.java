package br.com.indra.eduardo_duarte.controller;

import br.com.indra.eduardo_duarte.model.Produtos;
import br.com.indra.eduardo_duarte.service.ProdutosService;
import br.com.indra.eduardo_duarte.service.dto.ProdutoCreateDTO;
import br.com.indra.eduardo_duarte.service.dto.ProdutoUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutosController {

    private final ProdutosService produtosService;

    @GetMapping
    public ResponseEntity<List<Produtos>> listarProdutos() {
        return ResponseEntity.ok(produtosService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Produtos produto = produtosService.getById(id);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<?> buscarProdutosPorCategoria(@PathVariable Long idCategoria) {
        try {
            return ResponseEntity.ok(produtosService.buscarPorCategoria(idCategoria));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarProduto(@RequestBody ProdutoCreateDTO dto) {
        try {
            Produtos produtoCriado = produtosService.createdProduto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id,
                                              @RequestBody ProdutoUpdateDTO produtoUpdateDTO) {
        try {
            Produtos produtoAtualizado = produtosService.atualiza(id, produtoUpdateDTO);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }

    @PutMapping("/{id}/preco")
    public ResponseEntity<?> atualizarPreco(@PathVariable Long id,
                                            @RequestParam BigDecimal preco) {
        try {
            if (preco.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest()
                        .body("O preço não pode ser negativo");
            }

            Produtos produtoAtualizado = produtosService.atualizaPreco(id, preco);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProduto(@PathVariable Long id) {
        try {
            produtosService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<?> reativarProduto(@PathVariable Long id) {
        try {
            Produtos produtoReativado = produtosService.reativarProduto(id);
            return ResponseEntity.ok(produtoReativado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado");
        }
    }
}
