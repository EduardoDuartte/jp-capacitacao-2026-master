package br.com.indra.eduardo_duarte.model;

import jakarta.persistence.*;
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
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "codigo_barras")
    private String codigoBarras;

    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;

    @Column(name = "estoque_baixo")
    private Boolean estoqueBaixo;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();

        if (this.ativo == null) {
            this.ativo = true;
        }

        if (this.quantidadeEstoque == null) {
            this.quantidadeEstoque = 0;
        }

        if (this.estoqueMinimo == null) {
            this.estoqueMinimo = 0;
        }

        atualizarFlagEstoqueBaixo();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
        atualizarFlagEstoqueBaixo();
    }

    public void atualizarFlagEstoqueBaixo() {
        this.estoqueBaixo = this.quantidadeEstoque <= this.estoqueMinimo;
    }
}
