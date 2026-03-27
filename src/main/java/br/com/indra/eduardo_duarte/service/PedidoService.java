package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.Carrinho;
import br.com.indra.eduardo_duarte.model.ItemCarrinho;
import br.com.indra.eduardo_duarte.model.ItemPedido;
import br.com.indra.eduardo_duarte.model.Pedido;
import br.com.indra.eduardo_duarte.repository.CarrinhoRepository;
import br.com.indra.eduardo_duarte.repository.ItemCarrinhoRepository;
import br.com.indra.eduardo_duarte.repository.ItemPedidoRepository;
import br.com.indra.eduardo_duarte.repository.PedidoRepository;
import br.com.indra.eduardo_duarte.service.dto.CheckoutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final CarrinhoService carrinhoService;
    private final EstoqueService estoqueService;

    @Transactional
    public Pedido checkout(CheckoutDTO dto) {
        Carrinho carrinho = carrinhoRepository.findByIdUsuarioAndStatus(dto.getIdUsuario(), "ATIVO")
                .orElseThrow(() -> new RuntimeException("Carrinho ativo não encontrado"));

        List<ItemCarrinho> itensCarrinho = itemCarrinhoRepository.findByCarrinho_IdCarrinho(carrinho.getIdCarrinho());

        if (itensCarrinho.isEmpty()) {
            throw new RuntimeException("Carrinho está vazio");
        }

        BigDecimal desconto = dto.getDesconto() == null ? BigDecimal.ZERO : dto.getDesconto();
        BigDecimal frete = dto.getFrete() == null ? BigDecimal.ZERO : dto.getFrete();

        Pedido pedido = new Pedido();
        pedido.setIdUsuario(dto.getIdUsuario());
        pedido.setDesconto(desconto);
        pedido.setFrete(frete);
        pedido.setEndereco(dto.getEndereco());
        pedido.setStatus("CRIADO");

        BigDecimal totalItens = BigDecimal.ZERO;

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        for (ItemCarrinho itemCarrinho : itensCarrinho) {
            BigDecimal subtotal = itemCarrinho.getPriceSnapshot()
                    .multiply(BigDecimal.valueOf(itemCarrinho.getQuantidade()));

            totalItens = totalItens.add(subtotal);

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedidoSalvo);
            itemPedido.setProduto(itemCarrinho.getProduto());
            itemPedido.setQuantidade(itemCarrinho.getQuantidade());
            itemPedido.setPriceSnapshot(itemCarrinho.getPriceSnapshot());

            itemPedidoRepository.save(itemPedido);

            estoqueService.saidaEstoque(
                    itemCarrinho.getProduto().getId(),
                    itemCarrinho.getQuantidade(),
                    pedidoSalvo.getIdPedido(),
                    dto.getCriadoPor()
            );
        }

        BigDecimal totalFinal = totalItens.subtract(desconto).add(frete);

        if (totalFinal.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Total do pedido não pode ser negativo");
        }

        pedidoSalvo.setTotal(totalFinal);
        pedidoRepository.save(pedidoSalvo);

        carrinho.setStatus("FINALIZADO");
        carrinho.setValorTotal(BigDecimal.ZERO);
        carrinhoRepository.save(carrinho);

        itemCarrinhoRepository.deleteAll(itensCarrinho);

        return pedidoSalvo;
    }

    public Pedido buscarPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarPorUsuario(Long idUsuario) {
        return pedidoRepository.findByIdUsuario(idUsuario);
    }

    public List<ItemPedido> listarItensPedido(Long idPedido) {
        buscarPorId(idPedido);
        return itemPedidoRepository.findByPedido_IdPedido(idPedido);
    }

    public Pedido atualizarStatus(Long idPedido, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        validarStatus(novoStatus);

        String statusAtual = pedido.getStatus();
        String statusNovoUpper = novoStatus.toUpperCase();

        if ("CANCELADO".equals(statusNovoUpper)) {
            if (!"CRIADO".equalsIgnoreCase(statusAtual)
                    && !"PAGO".equalsIgnoreCase(statusAtual)) {
                throw new RuntimeException("Cancelamento permitido somente em CRIADO ou PAGO");
            }

            List<ItemPedido> itensPedido = itemPedidoRepository.findByPedido_IdPedido(idPedido);

            for (ItemPedido itemPedido : itensPedido) {
                estoqueService.devolucaoEstoque(
                        itemPedido.getProduto().getId(),
                        itemPedido.getQuantidade(),
                        pedido.getIdPedido(),
                        "cancelamento_pedido"
                );
            }
        }

        pedido.setStatus(statusNovoUpper);
        return pedidoRepository.save(pedido);
    }

    private void validarStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new RuntimeException("Status obrigatório");
        }

        String statusUpper = status.toUpperCase();

        boolean statusValido =
                "CRIADO".equals(statusUpper) ||
                        "PAGO".equals(statusUpper) ||
                        "CANCELADO".equals(statusUpper) ||
                        "ENVIADO".equals(statusUpper) ||
                        "FINALIZADO".equals(statusUpper);

        if (!statusValido) {
            throw new RuntimeException("Status inválido");
        }
    }
}
