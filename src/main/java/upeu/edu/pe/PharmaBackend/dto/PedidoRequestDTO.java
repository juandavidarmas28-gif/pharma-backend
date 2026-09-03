package upeu.edu.pe.PharmaBackend.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotEmpty(message = "La lista de detalles no puede estar vacía")
    @Valid
    private List<DetallePedidoRequestDTO> detalles;
}
