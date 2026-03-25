package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.HistoricoPreco;
import br.com.indra.eduardo_duarte.model.Produtos;
import br.com.indra.eduardo_duarte.repository.HistoricoPrecoRepository;
import br.com.indra.eduardo_duarte.repository.ProdutosRepository;
import br.com.indra.eduardo_duarte.service.dto.ProdutoUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;

    public List<Produtos> getAll() {
        return produtosRepository.findByAtivoTrue();
    }

    public Produtos createdProduto(Produtos produto) {
        produto.setAtivo(true);
        return produtosRepository.save(produto);
    }

    public Produtos atualiza(Long id, ProdutoUpdateDTO produtoAtualizado) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setCodigoBarras(produtoAtualizado.getCodigoBarras());

        return produtosRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        produto.setAtivo(false);
        produtosRepository.save(produto);
    }

    public Produtos reativarProduto(Long id) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setAtivo(true);
        return produtosRepository.save(produto);
    }

    public Produtos getById(Long id) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        return produto;
    }

    public Produtos atualizaPreco(Long id, BigDecimal preco) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Produto não encontrado");
        }

        BigDecimal precoAntigo = produto.getPreco();

        produto.setPreco(preco);

        HistoricoPreco historico = new HistoricoPreco();
        historico.setProdutos(produto);
        historico.setPrecoAntigo(precoAntigo);
        historico.setPrecoNovo(preco);

        historicoPrecoRepository.save(historico);

        return produtosRepository.save(produto);
    }
}
