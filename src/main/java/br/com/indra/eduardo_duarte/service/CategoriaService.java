package br.com.indra.eduardo_duarte.service;

import br.com.indra.eduardo_duarte.model.Categoria;
import br.com.indra.eduardo_duarte.repository.CategoriaRepository;
import br.com.indra.eduardo_duarte.service.dto.CategoriaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> getAll() {
        return categoriaRepository.findAll();
    }

    public Categoria getById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public Categoria criarCategoria(CategoriaRequestDTO dto) {
        validarNomeUnicoNoMesmoNivel(dto.getNome(), dto.getIdPai());

        Categoria categoriaPai = null;

        if (dto.getIdPai() != null) {
            categoriaPai = categoriaRepository.findById(dto.getIdPai())
                    .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada"));
        }

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setCategoriaPai(categoriaPai);

        return categoriaRepository.save(categoria);
    }

    public Categoria atualizarCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Categoria categoriaPai = null;

        if (dto.getIdPai() != null) {
            categoriaPai = categoriaRepository.findById(dto.getIdPai())
                    .orElseThrow(() -> new RuntimeException("Categoria pai não encontrada"));

            if (id.equals(dto.getIdPai())) {
                throw new RuntimeException("Categoria não pode ser pai dela mesma");
            }
        }

        boolean nomeDuplicado;

        if (dto.getIdPai() == null) {
            nomeDuplicado = categoriaRepository.existsByNomeAndCategoriaPaiIsNull(dto.getNome())
                    && !categoria.getNome().equals(dto.getNome());
        } else {
            nomeDuplicado = categoriaRepository.existsByNomeAndCategoriaPai_IdCategoria(dto.getNome(), dto.getIdPai())
                    && (!categoria.getNome().equals(dto.getNome())
                    || categoria.getCategoriaPai() == null
                    || !categoria.getCategoriaPai().getIdCategoria().equals(dto.getIdPai()));
        }

        if (nomeDuplicado) {
            throw new RuntimeException("Já existe uma categoria com esse nome no mesmo nível");
        }

        categoria.setNome(dto.getNome());
        categoria.setCategoriaPai(categoriaPai);

        return categoriaRepository.save(categoria);
    }

    public void deletarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoriaRepository.delete(categoria);
    }

    private void validarNomeUnicoNoMesmoNivel(String nome, Long idPai) {
        boolean existe;

        if (idPai == null) {
            existe = categoriaRepository.existsByNomeAndCategoriaPaiIsNull(nome);
        } else {
            existe = categoriaRepository.existsByNomeAndCategoriaPai_IdCategoria(nome, idPai);
        }

        if (existe) {
            throw new RuntimeException("Já existe uma categoria com esse nome no mesmo nível");
        }
    }
}
