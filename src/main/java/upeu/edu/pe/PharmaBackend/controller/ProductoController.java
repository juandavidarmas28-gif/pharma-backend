package upeu.edu.pe.PharmaBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upeu.edu.pe.PharmaBackend.dto.ProductoRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.ProductoResponseDTO;
import upeu.edu.pe.PharmaBackend.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> findAll() {
        return ResponseEntity.ok(productoService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.read(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO response = productoService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> update(@PathVariable Long id,
                                                        @Valid @RequestBody ProductoRequestDTO requestDTO) {
        return ResponseEntity.ok(productoService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
