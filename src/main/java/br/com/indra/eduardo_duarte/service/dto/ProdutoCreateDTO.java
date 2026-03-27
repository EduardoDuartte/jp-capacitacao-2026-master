package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoCreateDTO {

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String codigoBarras;
    private Long idCategoria;
}
