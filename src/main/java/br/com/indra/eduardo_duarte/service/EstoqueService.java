package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.Produtos;
import br.com.indra.eduardo_duarte.model.TransacaoEstoque;
import br.com.indra.eduardo_duarte.repository.ProdutosRepository;
import br.com.indra.eduardo_duarte.repository.TransacaoEstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final ProdutosRepository produtosRepository;
    private final TransacaoEstoqueRepository transacaoEstoqueRepository;

    public Produtos entradaEstoque(Long idProduto, Integer quantidade, Long referenceId, String criadoPor) {
        Produtos produto = buscarProdutoAtivo(idProduto);

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        produto.atualizarFlagEstoqueBaixo();

        produtosRepository.save(produto);

        criarTransacao(produto, quantidade, "ENTRADA", referenceId, criadoPor);

        return produto;
    }

    public Produtos saidaEstoque(Long idProduto, Integer quantidade, Long referenceId, String criadoPor) {
        Produtos produto = buscarProdutoAtivo(idProduto);

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        produto.atualizarFlagEstoqueBaixo();

        produtosRepository.save(produto);

        criarTransacao(produto, -quantidade, "SAIDA", referenceId, criadoPor);

        return produto;
    }

    public Produtos ajusteEstoque(Long idProduto, Integer novaQuantidade, Long referenceId, String criadoPor) {
        Produtos produto = buscarProdutoAtivo(idProduto);

        if (novaQuantidade < 0) {
            throw new RuntimeException("Quantidade não pode ser negativa");
        }

        int delta = novaQuantidade - produto.getQuantidadeEstoque();

        produto.setQuantidadeEstoque(novaQuantidade);
        produto.atualizarFlagEstoqueBaixo();

        produtosRepository.save(produto);

        criarTransacao(produto, delta, "AJUSTE", referenceId, criadoPor);

        return produto;
    }

    public Produtos devolucaoEstoque(Long idProduto, Integer quantidade, Long referenceId, String criadoPor) {
        Produtos produto = buscarProdutoAtivo(idProduto);

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        produto.atualizarFlagEstoqueBaixo();

        produtosRepository.save(produto);

        criarTransacao(produto, quantidade, "DEVOLUCAO", referenceId, criadoPor);

        return produto;
    }

    private Produtos buscarProdutoAtivo(Long idProduto) {
        Produtos produto = produtosRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        return produto;
    }

    public List<TransacaoEstoque> buscarTransacoesPorProduto(Long idProduto) {
        Produtos produto = produtosRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        return transacaoEstoqueRepository.findByProduto_Id(idProduto);
    }

    private void criarTransacao(Produtos produto, Integer delta, String reason, Long referenceId, String criadoPor) {
        TransacaoEstoque transacao = new TransacaoEstoque();
        transacao.setProduto(produto);
        transacao.setDelta(delta);
        transacao.setReason(reason);
        transacao.setReferenceId(referenceId);
        transacao.setCriadoPor(criadoPor);

        transacaoEstoqueRepository.save(transacao);
    }
}
