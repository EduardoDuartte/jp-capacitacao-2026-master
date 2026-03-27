package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemCarrinhoResponseDTO {

    private Long idItem;
    private Long idProduto;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal priceSnapshot;
    private BigDecimal subtotal;
}
