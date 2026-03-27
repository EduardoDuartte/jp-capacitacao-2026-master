package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.Categoria;
import br.com.indra.eduardo_duarte.model.HistoricoPreco;
import br.com.indra.eduardo_duarte.model.Produtos;
import br.com.indra.eduardo_duarte.repository.CategoriaRepository;
import br.com.indra.eduardo_duarte.repository.HistoricoPrecoRepository;
import br.com.indra.eduardo_duarte.repository.ProdutosRepository;
import br.com.indra.eduardo_duarte.service.dto.ProdutoCreateDTO;
import br.com.indra.eduardo_duarte.service.dto.ProdutoUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Produtos> getAll() {
        return produtosRepository.findByAtivoTrue();
    }

    public Produtos createdProduto(ProdutoCreateDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produtos produto = new Produtos();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCodigoBarras(dto.getCodigoBarras());
        produto.setAtivo(true);
        produto.setCategoria(categoria);

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

    public List<Produtos> buscarPorCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        List<Categoria> categoriasFilhas = categoriaRepository.findByCategoriaPai_IdCategoria(categoria.getIdCategoria());

        List<Long> idsCategorias = new ArrayList<>();
        idsCategorias.add(categoria.getIdCategoria());

        for (Categoria filha : categoriasFilhas) {
            idsCategorias.add(filha.getIdCategoria());
        }

        return produtosRepository.findByCategoria_IdCategoriaInAndAtivoTrue(idsCategorias);
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
