package br.com.indra.eduardo_duarte.controller;

import br.com.indra.eduardo_duarte.model.Carrinho;
import br.com.indra.eduardo_duarte.model.ItemCarrinho;
import br.com.indra.eduardo_duarte.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> criarOuBuscarCarrinhoAtivo(@PathVariable Long idUsuario) {
        try {
            Carrinho carrinho = carrinhoService.criarCarrinho(idUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(carrinho);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarCarrinhoAtivo(@PathVariable Long idUsuario) {
        try {
            Carrinho carrinho = carrinhoService.buscarCarrinhoAtivo(idUsuario);
            return ResponseEntity.ok(carrinho);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/usuario/{idUsuario}/itens")
    public ResponseEntity<?> adicionarItem(@PathVariable Long idUsuario,
                                           @RequestParam Long idProduto,
                                           @RequestParam Integer quantidade) {
        try {
            ItemCarrinho item = carrinhoService.adicionarItem(idUsuario, idProduto, quantidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idCarrinho}/itens/{idProduto}")
    public ResponseEntity<?> atualizarQuantidadeItem(@PathVariable Long idCarrinho,
                                                     @PathVariable Long idProduto,
                                                     @RequestParam Integer quantidade) {
        try {
            ItemCarrinho item = carrinhoService.atualizarQuantidadeItem(idCarrinho, idProduto, quantidade);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{idCarrinho}/itens/{idProduto}")
    public ResponseEntity<?> removerItem(@PathVariable Long idCarrinho,
                                         @PathVariable Long idProduto) {
        try {
            carrinhoService.removerItem(idCarrinho, idProduto);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{idCarrinho}/itens")
    public ResponseEntity<?> listarItensCarrinho(@PathVariable Long idCarrinho) {
        try {
            List<ItemCarrinho> itens = carrinhoService.listarItensCarrinho(idCarrinho);
            return ResponseEntity.ok(itens);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idCarrinho}")
    public ResponseEntity<?> visualizarCarrinho(@PathVariable Long idCarrinho) {
        try {
            return ResponseEntity.ok(carrinhoService.visualizarCarrinho(idCarrinho));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
