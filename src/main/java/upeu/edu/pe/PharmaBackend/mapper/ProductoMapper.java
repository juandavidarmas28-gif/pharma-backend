package upeu.edu.pe.PharmaBackend.mapper;

import org.springframework.stereotype.Component;
import upeu.edu.pe.PharmaBackend.dto.CategoriaResumenDTO;
import upeu.edu.pe.PharmaBackend.dto.ProductoRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.ProductoResponseDTO;
import upeu.edu.pe.PharmaBackend.entity.Categoria;
import upeu.edu.pe.PharmaBackend.entity.Producto;

/**
 * Encapsula la transformacion entre la entidad Producto y sus DTOs de
 * entrada/salida, incluyendo el resumen de la categoria asociada.
 */
@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequestDTO dto, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);
        return producto;
    }

    public void actualizarEntidad(Producto producto, ProductoRequestDTO dto, Categoria categoria) {
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoria(toCategoriaResumenDTO(producto.getCategoria()));
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaModificacion(producto.getFechaModificacion());
        return dto;
    }

    public CategoriaResumenDTO toCategoriaResumenDTO(Categoria categoria) {
        return new CategoriaResumenDTO(categoria.getId(), categoria.getNombre());
    }
}
