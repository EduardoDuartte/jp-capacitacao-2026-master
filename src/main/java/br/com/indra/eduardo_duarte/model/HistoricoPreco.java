package br.com.indra.eduardo_duarte.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historico_preco")
public class HistoricoPreco {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "produtos_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Produtos produtos;

    @Column(name = "preco_antigo")
    private BigDecimal precoAntigo;

    @Column(name = "preco_novo")
    private BigDecimal precoNovo;

    @Column(name = "data_alteracao", updatable = false)
    @CreationTimestamp
    private LocalDateTime dataAlteracao;
}