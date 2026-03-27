package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CheckoutDTO {

    private Long idUsuario;
    private BigDecimal desconto;
    private BigDecimal frete;
    private String endereco;
    private String criadoPor;
}
