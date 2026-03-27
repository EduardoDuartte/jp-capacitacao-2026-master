package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.Carrinho;
import br.com.indra.eduardo_duarte.model.ItemCarrinho;
import br.com.indra.eduardo_duarte.model.Produtos;
import br.com.indra.eduardo_duarte.repository.CarrinhoRepository;
import br.com.indra.eduardo_duarte.repository.ItemCarrinhoRepository;
import br.com.indra.eduardo_duarte.repository.ProdutosRepository;
import br.com.indra.eduardo_duarte.service.dto.CarrinhoResponseDTO;
import br.com.indra.eduardo_duarte.service.dto.ItemCarrinhoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ProdutosRepository produtosRepository;

    public Carrinho criarCarrinho(Long idUsuario) {
        return carrinhoRepository.findByIdUsuarioAndStatus(idUsuario, "ATIVO")
                .orElseGet(() -> {
                    Carrinho carrinho = new Carrinho();
                    carrinho.setIdUsuario(idUsuario);
                    carrinho.setStatus("ATIVO");
                    carrinho.setValorTotal(BigDecimal.ZERO);

                    return carrinhoRepository.save(carrinho);
                });
    }

    public Carrinho buscarCarrinhoAtivo(Long idUsuario) {
        return carrinhoRepository.findByIdUsuarioAndStatus(idUsuario, "ATIVO")
                .orElseThrow(() -> new RuntimeException("Carrinho ativo não encontrado"));
    }

    public ItemCarrinho adicionarItem(Long idUsuario, Long idProduto, Integer quantidade) {
        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        Carrinho carrinho = criarCarrinho(idUsuario);

        Produtos produto = produtosRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        ItemCarrinho item = itemCarrinhoRepository
                .findByCarrinho_IdCarrinhoAndProduto_Id(carrinho.getIdCarrinho(), idProduto)
                .orElse(null);

        if (item == null) {
            item = new ItemCarrinho();
            item.setCarrinho(carrinho);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPriceSnapshot(produto.getPreco());
        } else {
            int novaQuantidade = item.getQuantidade() + quantidade;

            if (produto.getQuantidadeEstoque() < novaQuantidade) {
                throw new RuntimeException("Estoque insuficiente");
            }

            item.setQuantidade(novaQuantidade);
            item.setPriceSnapshot(produto.getPreco());
        }

        ItemCarrinho itemSalvo = itemCarrinhoRepository.save(item);
        recalcularTotalCarrinho(carrinho);

        return itemSalvo;
    }

    public ItemCarrinho atualizarQuantidadeItem(Long idCarrinho, Long idProduto, Integer novaQuantidade) {
        if (novaQuantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        ItemCarrinho item = itemCarrinhoRepository
                .findByCarrinho_IdCarrinhoAndProduto_Id(idCarrinho, idProduto)
                .orElseThrow(() -> new RuntimeException("Item do carrinho não encontrado"));

        Produtos produto = item.getProduto();

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        if (produto.getQuantidadeEstoque() < novaQuantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        item.setQuantidade(novaQuantidade);
        item.setPriceSnapshot(produto.getPreco());

        ItemCarrinho itemSalvo = itemCarrinhoRepository.save(item);
        recalcularTotalCarrinho(item.getCarrinho());

        return itemSalvo;
    }

    public void removerItem(Long idCarrinho, Long idProduto) {
        ItemCarrinho item = itemCarrinhoRepository
                .findByCarrinho_IdCarrinhoAndProduto_Id(idCarrinho, idProduto)
                .orElseThrow(() -> new RuntimeException("Item do carrinho não encontrado"));

        Carrinho carrinho = item.getCarrinho();

        itemCarrinhoRepository.delete(item);
        recalcularTotalCarrinho(carrinho);
    }

    public List<ItemCarrinho> listarItensCarrinho(Long idCarrinho) {
        return itemCarrinhoRepository.findByCarrinho_IdCarrinho(idCarrinho);
    }

    public void recalcularTotalCarrinho(Carrinho carrinho) {
        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinho_IdCarrinho(carrinho.getIdCarrinho());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrinho item : itens) {
            BigDecimal subtotal = item.getPriceSnapshot()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            total = total.add(subtotal);
        }

        carrinho.setValorTotal(total);
        carrinhoRepository.save(carrinho);
    }

    public CarrinhoResponseDTO visualizarCarrinho(Long idCarrinho) {
        Carrinho carrinho = carrinhoRepository.findById(idCarrinho)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinho_IdCarrinho(idCarrinho);

        CarrinhoResponseDTO response = new CarrinhoResponseDTO();
        response.setIdCarrinho(carrinho.getIdCarrinho());
        response.setIdUsuario(carrinho.getIdUsuario());
        response.setStatus(carrinho.getStatus());
        response.setValorTotal(carrinho.getValorTotal());

        List<ItemCarrinhoResponseDTO> itensResponse = new ArrayList<>();

        for (ItemCarrinho item : itens) {
            ItemCarrinhoResponseDTO itemDTO = new ItemCarrinhoResponseDTO();
            itemDTO.setIdItem(item.getIdItem());
            itemDTO.setIdProduto(item.getProduto().getId());
            itemDTO.setNomeProduto(item.getProduto().getNome());
            itemDTO.setQuantidade(item.getQuantidade());
            itemDTO.setPriceSnapshot(item.getPriceSnapshot());
            itemDTO.setSubtotal(
                    item.getPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantidade()))
            );

            itensResponse.add(itemDTO);
        }

        response.setItens(itensResponse);

        return response;
    }
}
