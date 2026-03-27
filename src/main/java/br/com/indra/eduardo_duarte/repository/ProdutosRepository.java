package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findByAtivoTrue();

    List<Produtos> findByCategoria_IdCategoriaAndAtivoTrue(Long idCategoria);

    List<Produtos> findByCategoria_IdCategoriaInAndAtivoTrue(List<Long> idsCategorias);
}
