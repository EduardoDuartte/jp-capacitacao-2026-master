package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentacaoEstoqueDTO {

    private Long idProduto;
    private Integer quantidade;
    private Long referenceId;
    private String criadoPor;
}
