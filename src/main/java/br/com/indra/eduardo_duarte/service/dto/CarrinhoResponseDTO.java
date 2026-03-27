package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CarrinhoResponseDTO {

    private Long idCarrinho;
    private Long idUsuario;
    private String status;
    private BigDecimal valorTotal;
    private List<ItemCarrinhoResponseDTO> itens;
}
