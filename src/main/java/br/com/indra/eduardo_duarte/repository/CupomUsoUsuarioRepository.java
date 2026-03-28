package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.CupomUsoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomUsoUsuarioRepository extends JpaRepository<CupomUsoUsuario, Long> {

    boolean existsByCupom_IdCupomAndIdUsuario(Long idCupom, Long idUsuario);
}