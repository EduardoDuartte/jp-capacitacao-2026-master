package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedido_IdPedido(Long idPedido);
}
