package br.com.indra.eduardo_duarte.controller;


import br.com.indra.eduardo_duarte.model.Produtos;
import br.com.indra.eduardo_duarte.service.EstoqueService;
import br.com.indra.eduardo_duarte.service.dto.AjusteEstoqueDTO;
import br.com.indra.eduardo_duarte.service.dto.EntradaEstoqueDTO;
import br.com.indra.eduardo_duarte.service.dto.MovimentacaoEstoqueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping("/entrada")
    public ResponseEntity<?> entradaEstoque(@RequestBody EntradaEstoqueDTO dto) {
        try {
            Produtos produtoAtualizado = estoqueService.entradaEstoque(
                    dto.getIdProduto(),
                    dto.getQuantidade(),
                    dto.getReferenceId(),
                    dto.getCriadoPor()
            );

            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/saida")
    public ResponseEntity<?> saidaEstoque(@RequestBody MovimentacaoEstoqueDTO dto) {
        try {
            Produtos produtoAtualizado = estoqueService.saidaEstoque(
                    dto.getIdProduto(),
                    dto.getQuantidade(),
                    dto.getReferenceId(),
                    dto.getCriadoPor()
            );

            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/ajuste")
    public ResponseEntity<?> ajusteEstoque(@RequestBody AjusteEstoqueDTO dto) {
        try {
            Produtos produtoAtualizado = estoqueService.ajusteEstoque(
                    dto.getIdProduto(),
                    dto.getNovaQuantidade(),
                    dto.getReferenceId(),
                    dto.getCriadoPor()
            );

            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/devolucao")
    public ResponseEntity<?> devolucaoEstoque(@RequestBody MovimentacaoEstoqueDTO dto) {
        try {
            Produtos produtoAtualizado = estoqueService.devolucaoEstoque(
                    dto.getIdProduto(),
                    dto.getQuantidade(),
                    dto.getReferenceId(),
                    dto.getCriadoPor()
            );

            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/produto/{idProduto}/transacoes")
    public ResponseEntity<?> buscarTransacoesPorProduto(@PathVariable Long idProduto) {
        try {
            return ResponseEntity.ok(estoqueService.buscarTransacoesPorProduto(idProduto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
