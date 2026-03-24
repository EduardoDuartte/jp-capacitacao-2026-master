package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.HistoricoPreco;
import br.com.indra.eduardo_duarte.repository.HistoricoPrecoRepository;
import br.com.indra.eduardo_duarte.service.dto.HistoricoProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    public List<HistoricoProdutoDTO> getHistoricoByProdutoId(Long produtoId) {
        List<HistoricoPreco> historicos = historicoPrecoRepository.findByProdutosId(produtoId);

        List<HistoricoProdutoDTO> historicoDTOs = new ArrayList<>();

        for (HistoricoPreco historico : historicos) {
            HistoricoProdutoDTO dto = new HistoricoProdutoDTO();
            dto.setId(historico.getId());
            dto.setProduto(historico.getProdutos().getNome());
            dto.setPrecoAntigo(historico.getPrecoAntigo());
            dto.setPrecoNovo(historico.getPrecoNovo());
            dto.setDataRegistro(historico.getDataAlteracao());

            historicoDTOs.add(dto);
        }

        return historicoDTOs;
    }
}