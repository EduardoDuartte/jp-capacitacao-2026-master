package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByIdUsuario(Long idUsuario);
}
