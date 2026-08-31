package upeu.edu.pe.PharmaBackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.PharmaBackend.dto.ProductoRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.ProductoResponseDTO;
import upeu.edu.pe.PharmaBackend.entity.Categoria;
import upeu.edu.pe.PharmaBackend.entity.Producto;
import upeu.edu.pe.PharmaBackend.exception.RecursoNoEncontradoException;
import upeu.edu.pe.PharmaBackend.mapper.ProductoMapper;
import upeu.edu.pe.PharmaBackend.repository.CategoriaRepository;
import upeu.edu.pe.PharmaBackend.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository,
                            CategoriaRepository categoriaRepository,
                            ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoMapper = productoMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> readAll() {
        log.info("Listando todos los productos");
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO read(Long id) {
        log.info("Buscando producto con id={}", id);
        Producto producto = buscarProductoOLanzar(id);
        return productoMapper.toResponseDTO(producto);
    }

    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO requestDTO) {
        log.info("Creando producto '{}' para categoriaId={}", requestDTO.getNombre(), requestDTO.getCategoriaId());

        // Paso 6: validar que la categoria referenciada exista antes de crear el producto
        Categoria categoria = buscarCategoriaOLanzar(requestDTO.getCategoriaId());

        Producto producto = productoMapper.toEntity(requestDTO, categoria);
        Producto guardado = productoRepository.save(producto);

        log.info("Producto creado con id={} asociado a categoriaId={}", guardado.getId(), categoria.getId());
        return productoMapper.toResponseDTO(guardado);
    }

    @Transactional
    public ProductoResponseDTO update(Long id, ProductoRequestDTO requestDTO) {
        log.info("Actualizando producto id={}", id);
        Producto producto = buscarProductoOLanzar(id);

        // Paso 6: validar la nueva categoria referenciada antes de modificar el producto
        Categoria categoria = buscarCategoriaOLanzar(requestDTO.getCategoriaId());

        productoMapper.actualizarEntidad(producto, requestDTO, categoria);
        Producto actualizado = productoRepository.save(producto);

        log.info("Producto id={} actualizado correctamente", id);
        return productoMapper.toResponseDTO(actualizado);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Eliminando producto id={}", id);
        Producto producto = buscarProductoOLanzar(id);
        productoRepository.delete(producto);
        log.info("Producto id={} eliminado correctamente", id);
    }

    private Producto buscarProductoOLanzar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el producto con id " + id));
    }

    private Categoria buscarCategoriaOLanzar(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> {
                    log.warn("Categoria con id={} no existe. Se rechaza la operacion sobre el producto.", categoriaId);
                    return new RecursoNoEncontradoException(
                            "No se encontro la categoria con id " + categoriaId);
                });
    }
}
