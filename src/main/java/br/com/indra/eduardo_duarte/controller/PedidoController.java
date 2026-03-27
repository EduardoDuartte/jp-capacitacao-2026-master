package br.com.indra.eduardo_duarte.controller;

import br.com.indra.eduardo_duarte.model.ItemPedido;
import br.com.indra.eduardo_duarte.model.Pedido;
import br.com.indra.eduardo_duarte.service.PedidoService;
import br.com.indra.eduardo_duarte.service.dto.AtualizarStatusPedidoDTO;
import br.com.indra.eduardo_duarte.service.dto.CheckoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutDTO dto) {
        try {
            Pedido pedido = pedidoService.checkout(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable Long idPedido) {
        try {
            return ResponseEntity.ok(pedidoService.buscarPorId(idPedido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pedido>> listarPedidosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{idPedido}/itens")
    public ResponseEntity<?> listarItensPedido(@PathVariable Long idPedido) {
        try {
            List<ItemPedido> itens = pedidoService.listarItensPedido(idPedido);
            return ResponseEntity.ok(itens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{idPedido}/status")
    public ResponseEntity<?> atualizarStatusPedido(@PathVariable Long idPedido,
                                                   @RequestBody AtualizarStatusPedidoDTO dto) {
        try {
            Pedido pedido = pedidoService.atualizarStatus(idPedido, dto.getStatus());
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
