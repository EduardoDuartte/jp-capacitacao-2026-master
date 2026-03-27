package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNomeAndCategoriaPaiIsNull(String nome);

    boolean existsByNomeAndCategoriaPai_IdCategoria(String nome, Long idPai);

    List<Categoria> findByCategoriaPai_IdCategoria(Long idCategoriaPai);
}
