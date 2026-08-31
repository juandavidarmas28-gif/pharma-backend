package upeu.edu.pe.PharmaBackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import upeu.edu.pe.PharmaBackend.dto.CategoriaRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.CategoriaResponseDTO;
import upeu.edu.pe.PharmaBackend.entity.Categoria;
import upeu.edu.pe.PharmaBackend.exception.RecursoNoEncontradoException;
import upeu.edu.pe.PharmaBackend.repository.CategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponseDTO> readAll() {
        log.info("Listando todas las categorias");
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO read(Long id) {
        log.info("Buscando categoria con id={}", id);
        Categoria categoria = buscarOLanzar(id);
        return toResponseDTO(categoria);
    }

    public CategoriaResponseDTO create(CategoriaRequestDTO requestDTO) {
        log.info("Creando categoria: {}", requestDTO.getNombre());
        Categoria categoria = new Categoria();
        categoria.setNombre(requestDTO.getNombre());
        categoria.setDescripcion(requestDTO.getDescripcion());
        categoria.setEstado(requestDTO.getEstado());
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoria creada con id={}", guardada.getId());
        return toResponseDTO(guardada);
    }

    public CategoriaResponseDTO update(Long id, CategoriaRequestDTO requestDTO) {
        log.info("Actualizando categoria con id={}", id);
        Categoria categoria = buscarOLanzar(id);
        categoria.setNombre(requestDTO.getNombre());
        categoria.setDescripcion(requestDTO.getDescripcion());
        categoria.setEstado(requestDTO.getEstado());
        Categoria actualizada = categoriaRepository.save(categoria);
        log.info("Categoria id={} actualizada correctamente", id);
        return toResponseDTO(actualizada);
    }

    public void delete(Long id) {
        log.info("Eliminando categoria con id={}", id);
        Categoria categoria = buscarOLanzar(id);
        categoriaRepository.delete(categoria);
        log.info("Categoria id={} eliminada correctamente", id);
    }

    private Categoria buscarOLanzar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la categoria con id " + id));
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado(),
                categoria.getFechaCreacion(),
                categoria.getFechaModificacion()
        );
    }
}
