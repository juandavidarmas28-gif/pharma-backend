package upeu.edu.pe.PharmaBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private CategoriaResumenDTO categoria;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
