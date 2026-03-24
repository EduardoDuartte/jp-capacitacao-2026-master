package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
}