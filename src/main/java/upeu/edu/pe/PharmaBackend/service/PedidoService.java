package upeu.edu.pe.PharmaBackend.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upeu.edu.pe.PharmaBackend.dto.*;
import upeu.edu.pe.PharmaBackend.entity.*;
import upeu.edu.pe.PharmaBackend.exception.BusinessException;
import upeu.edu.pe.PharmaBackend.exception.StockInsuficienteException;
import upeu.edu.pe.PharmaBackend.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public PedidoResponseDTO registrarPedido(PedidoRequestDTO request) {
        log.info("Iniciando registro transaccional de pedido para el cliente ID: {}", request.getClienteId());


        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> {
                    log.error("Error al registrar pedido: Cliente ID {} no encontrado", request.getClienteId());
                    return new BusinessException("Cliente no encontrado con ID: " + request.getClienteId());
                });


        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BusinessException("El pedido debe contener al menos un detalle");
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .fecha(LocalDateTime.now())
                .estado("COMPLETADO")
                .total(BigDecimal.ZERO)
                .detalles(new ArrayList<>())
                .build();

        BigDecimal acumuladoTotal = BigDecimal.ZERO;
        List<DetallePedidoResponseDTO> detallesDTO = new ArrayList<>();

        for (DetallePedidoRequestDTO detReq : request.getDetalles()) {

            Producto producto = productoRepository.findById(detReq.getProductoId())
                    .orElseThrow(() -> {
                        log.error("Error al procesar producto ID {}: Producto no encontrado", detReq.getProductoId());
                        return new BusinessException("Producto no encontrado con ID: " + detReq.getProductoId());
                    });

            log.info("Procesando producto ID: {}, Nombre: {}, Stock actual: {}",
                    producto.getId(), producto.getNombre(), producto.getStock());


            if (detReq.getCantidad() == null || detReq.getCantidad() <= 0) {
                throw new BusinessException("La cantidad debe ser mayor a 0 para el producto ID: " + producto.getId());
            }


            if (producto.getStock() < detReq.getCantidad()) {
                log.error("Stock insuficiente para el producto ID {}. Solicitado: {}, Disponible: {}",
                        producto.getId(), detReq.getCantidad(), producto.getStock());
                throw new StockInsuficienteException(
                        String.format("Stock insuficiente para el producto '%s' (ID: %d). Stock disponible: %d, solicitado: %d",
                                producto.getNombre(), producto.getId(), producto.getStock(), detReq.getCantidad())
                );
            }

            BigDecimal precioUnitario = producto.getPrecio();


            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detReq.getCantidad()));

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(detReq.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .build();

            pedido.addDetalle(detalle);


            producto.setStock(producto.getStock() - detReq.getCantidad());
            productoRepository.save(producto);

            acumuladoTotal = acumuladoTotal.add(subtotal);

            detallesDTO.add(DetallePedidoResponseDTO.builder()
                    .productoId(producto.getId())
                    .productoNombre(producto.getNombre())
                    .cantidad(detReq.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .build());
        }


        pedido.setTotal(acumuladoTotal);


        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        log.info("Pedido registrado exitosamente con ID: {}, Total: {}", pedidoGuardado.getId(), pedidoGuardado.getTotal());

        return PedidoResponseDTO.builder()
                .id(pedidoGuardado.getId())
                .fecha(pedidoGuardado.getFecha())
                .clienteId(cliente.getId())
                .estado(pedidoGuardado.getEstado())
                .total(pedidoGuardado.getTotal())
                .detalles(detallesDTO)
                .build();
    }
}
