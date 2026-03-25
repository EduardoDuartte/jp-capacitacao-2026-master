package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoUpdateDTO {
    private String nome;
    private String descricao;
    private String codigoBarras;
}
