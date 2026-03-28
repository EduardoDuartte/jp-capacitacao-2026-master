package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.Cupom;
import br.com.indra.eduardo_duarte.model.CupomUsoUsuario;
import br.com.indra.eduardo_duarte.model.ItemCarrinho;
import br.com.indra.eduardo_duarte.repository.CupomRepository;
import br.com.indra.eduardo_duarte.repository.CupomUsoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CupomService {

    private final CupomRepository cupomRepository;
    private final CupomUsoUsuarioRepository cupomUsoUsuarioRepository;

    public List<Cupom> getAll() {
        return cupomRepository.findAll();
    }

    public Cupom getById(Long id) {
        return cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
    }

    public Cupom getByCodigo(String codigo) {
        return cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
    }

    public Cupom criarCupom(Cupom cupom) {
        boolean codigoJaExiste = cupomRepository.findByCodigo(cupom.getCodigo()).isPresent();

        if (codigoJaExiste) {
            throw new RuntimeException("Já existe um cupom com esse código");
        }
        cupom.setIdCupom(null);

        return cupomRepository.save(cupom);
    }

    public Cupom atualizarCupom(Long id, Cupom cupomAtualizado) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        if (!cupom.getCodigo().equals(cupomAtualizado.getCodigo())) {
            boolean codigoJaExiste = cupomRepository.findByCodigo(cupomAtualizado.getCodigo()).isPresent();

            if (codigoJaExiste) {
                throw new RuntimeException("Já existe um cupom com esse código");
            }
        }

        cupom.setCodigo(cupomAtualizado.getCodigo());
        cupom.setTipo(cupomAtualizado.getTipo());
        cupom.setValor(cupomAtualizado.getValor());
        cupom.setDataExpiracao(cupomAtualizado.getDataExpiracao());
        cupom.setLimiteUso(cupomAtualizado.getLimiteUso());
        cupom.setContagemUso(cupomAtualizado.getContagemUso());
        cupom.setAplicavelA(cupomAtualizado.getAplicavelA());
        cupom.setIdCategoria(cupomAtualizado.getIdCategoria());
        cupom.setIdProduto(cupomAtualizado.getIdProduto());

        return cupomRepository.save(cupom);
    }

    public void deletarCupom(Long id) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        cupomRepository.delete(cupom);
    }

    public BigDecimal validarECalcularDesconto(String codigoCupom, Long idUsuario, List<ItemCarrinho> itensCarrinho) {
        if (codigoCupom == null || codigoCupom.isBlank()) {
            return BigDecimal.ZERO;
        }

        Cupom cupom = cupomRepository.findByCodigo(codigoCupom)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        validarCupom(cupom, idUsuario, itensCarrinho);

        BigDecimal baseDesconto = calcularBaseDesconto(cupom, itensCarrinho);

        if ("PERCENTUAL".equalsIgnoreCase(cupom.getTipo())) {
            return baseDesconto
                    .multiply(cupom.getValor())
                    .divide(BigDecimal.valueOf(100));
        }

        if ("FIXO".equalsIgnoreCase(cupom.getTipo())) {
            if (cupom.getValor().compareTo(baseDesconto) > 0) {
                return baseDesconto;
            }

            return cupom.getValor();
        }

        throw new RuntimeException("Tipo de cupom inválido");
    }

    public void registrarUsoCupom(String codigoCupom, Long idUsuario) {
        if (codigoCupom == null || codigoCupom.isBlank()) {
            return;
        }

        Cupom cupom = cupomRepository.findByCodigo(codigoCupom)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        CupomUsoUsuario uso = new CupomUsoUsuario();
        uso.setCupom(cupom);
        uso.setIdUsuario(idUsuario);

        cupomUsoUsuarioRepository.save(uso);

        cupom.setContagemUso(cupom.getContagemUso() + 1);
        cupomRepository.save(cupom);
    }

    private void validarCupom(Cupom cupom, Long idUsuario, List<ItemCarrinho> itensCarrinho) {
        LocalDateTime agora = LocalDateTime.now();

        if (cupom.getDataCriacao() != null && agora.isBefore(cupom.getDataCriacao())) {
            throw new RuntimeException("Cupom ainda não está válido");
        }

        if (cupom.getDataExpiracao() != null && agora.isAfter(cupom.getDataExpiracao())) {
            throw new RuntimeException("Cupom expirado");
        }

        if (cupom.getContagemUso() >= cupom.getLimiteUso()) {
            throw new RuntimeException("Limite de uso do cupom atingido");
        }

        boolean usuarioJaUsou = cupomUsoUsuarioRepository
                .existsByCupom_IdCupomAndIdUsuario(cupom.getIdCupom(), idUsuario);

        if (usuarioJaUsou) {
            throw new RuntimeException("Cupom já utilizado pelo usuário");
        }

        boolean aplicavel = verificarAplicabilidade(cupom, itensCarrinho);

        if (!aplicavel) {
            throw new RuntimeException("Cupom não aplicável aos itens do carrinho");
        }
    }

    private boolean verificarAplicabilidade(Cupom cupom, List<ItemCarrinho> itensCarrinho) {
        if ("PEDIDO".equalsIgnoreCase(cupom.getAplicavelA())) {
            return true;
        }

        if ("PRODUTO".equalsIgnoreCase(cupom.getAplicavelA())) {
            for (ItemCarrinho item : itensCarrinho) {
                if (item.getProduto().getId().equals(cupom.getIdProduto())) {
                    return true;
                }
            }
            return false;
        }

        if ("CATEGORIA".equalsIgnoreCase(cupom.getAplicavelA())) {
            for (ItemCarrinho item : itensCarrinho) {
                if (item.getProduto().getCategoria().getIdCategoria().equals(cupom.getIdCategoria())) {
                    return true;
                }
            }
            return false;
        }

        throw new RuntimeException("Tipo de aplicabilidade inválido");
    }

    private BigDecimal calcularBaseDesconto(Cupom cupom, List<ItemCarrinho> itensCarrinho) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrinho item : itensCarrinho) {
            boolean incluir = false;

            if ("PEDIDO".equalsIgnoreCase(cupom.getAplicavelA())) {
                incluir = true;
            } else if ("PRODUTO".equalsIgnoreCase(cupom.getAplicavelA())
                    && item.getProduto().getId().equals(cupom.getIdProduto())) {
                incluir = true;
            } else if ("CATEGORIA".equalsIgnoreCase(cupom.getAplicavelA())
                    && item.getProduto().getCategoria().getIdCategoria().equals(cupom.getIdCategoria())) {
                incluir = true;
            }

            if (incluir) {
                BigDecimal subtotal = item.getPriceSnapshot()
                        .multiply(BigDecimal.valueOf(item.getQuantidade()));

                total = total.add(subtotal);
            }
        }

        return total;
    }
}