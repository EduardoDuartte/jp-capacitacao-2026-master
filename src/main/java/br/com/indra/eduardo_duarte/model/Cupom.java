package br.com.indra.eduardo_duarte.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cupons")
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cupom")
    private Long idCupom;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Column(name = "limite_uso")
    private Integer limiteUso;

    @Column(name = "contagem_uso")
    private Integer contagemUso;

    @Column(name = "aplicavel_a")
    private String aplicavelA;

    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "id_produto")
    private Long idProduto;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();

        if (this.contagemUso == null) {
            this.contagemUso = 0;
        }

        if (this.limiteUso == null) {
            this.limiteUso = 1;
        }
    }
}