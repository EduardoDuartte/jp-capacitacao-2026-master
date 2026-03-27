package br.com.indra.eduardo_duarte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrinho")
    private Long idCarrinho;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private String status;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @PrePersist
    public void prePersist() {
        if (this.valorTotal == null) {
            this.valorTotal = BigDecimal.ZERO;
        }

        if (this.status == null) {
            this.status = "ATIVO";
        }
    }
}
