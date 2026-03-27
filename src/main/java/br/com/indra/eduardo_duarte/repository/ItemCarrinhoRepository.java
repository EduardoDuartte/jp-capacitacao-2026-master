package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    List<ItemCarrinho> findByCarrinho_IdCarrinho(Long idCarrinho);

    Optional<ItemCarrinho> findByCarrinho_IdCarrinhoAndProduto_Id(Long idCarrinho, Long idProduto);
}
