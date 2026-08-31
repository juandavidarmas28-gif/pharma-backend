package upeu.edu.pe.PharmaBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Resumen de la categoria que se incluye dentro de la respuesta de un producto.
 * Solo expone lo necesario, sin datos internos ni la lista de productos
 * (evita referencias circulares Producto <-> Categoria).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResumenDTO {
    private Long id;
    private String nombre;
}
