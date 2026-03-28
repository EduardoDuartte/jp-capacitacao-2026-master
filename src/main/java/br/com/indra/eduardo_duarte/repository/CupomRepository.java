package br.com.indra.eduardo_duarte.repository;

import br.com.indra.eduardo_duarte.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    Optional<Cupom> findByCodigo(String codigo);
}