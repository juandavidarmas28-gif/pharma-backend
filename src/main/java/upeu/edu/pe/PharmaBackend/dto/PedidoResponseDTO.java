package upeu.edu.pe.PharmaBackend.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private Long clienteId;
    private String estado;
    private BigDecimal total;
    private List<DetallePedidoResponseDTO> detalles;
}
