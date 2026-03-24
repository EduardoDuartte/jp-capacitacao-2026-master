package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, UUID> {

    List<HistoricoPreco> findByProdutosId(Long produtoId);

}