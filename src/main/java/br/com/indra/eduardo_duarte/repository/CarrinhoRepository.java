package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    List<Carrinho> findByIdUsuario(Long idUsuario);

    Optional<Carrinho> findByIdUsuarioAndStatus(Long idUsuario, String status);
}
