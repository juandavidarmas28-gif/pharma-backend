package upeu.edu.pe.PharmaBackend.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.PharmaBackend.dto.PedidoRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.PedidoResponseDTO;
import upeu.edu.pe.PharmaBackend.service.PedidoService;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> registrarPedido(@Valid @RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO response = pedidoService.registrarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
