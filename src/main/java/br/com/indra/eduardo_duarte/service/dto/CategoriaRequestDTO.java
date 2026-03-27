package br.com.indra.eduardo_duarte.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequestDTO {

    private String nome;
    private Long idPai;
}
