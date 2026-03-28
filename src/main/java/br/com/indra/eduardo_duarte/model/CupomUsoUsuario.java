package br.com.indra.eduardo_duarte.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cupom_uso_usuario")
public class CupomUsoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uso")
    private Long idUso;

    @ManyToOne
    @JoinColumn(name = "id_cupom", nullable = false)
    private Cupom cupom;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "usado_em", updatable = false, nullable = false)
    private LocalDateTime usadoEm;

    @PrePersist
    public void prePersist() {
        this.usadoEm = LocalDateTime.now();
    }
}