package upeu.edu.pe.PharmaBackend.service;
import upeu.edu.pe.PharmaBackend.dto.VentaRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.VentaResponseDTO;

import java.util.List;

public interface VentaService {
    VentaResponseDTO registrar(VentaRequestDTO request);
    VentaResponseDTO buscar(Long id);
    List<VentaResponseDTO> listar();
}