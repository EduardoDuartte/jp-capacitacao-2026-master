package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AjusteEstoqueDTO {

    private Long idProduto;
    private Integer novaQuantidade;
    private Long referenceId;
    private String criadoPor;
}
