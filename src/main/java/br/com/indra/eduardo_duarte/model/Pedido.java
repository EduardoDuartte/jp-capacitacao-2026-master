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
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private BigDecimal desconto;

    @Column(nullable = false)
    private BigDecimal frete;

    @Column(nullable = false)
    private String status;

    @Column(name = "criado_em", updatable = false, nullable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private String endereco;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();

        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }

        if (this.desconto == null) {
            this.desconto = BigDecimal.ZERO;
        }

        if (this.frete == null) {
            this.frete = BigDecimal.ZERO;
        }

        if (this.status == null) {
            this.status = "CRIADO";
        }
    }
}
