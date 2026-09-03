package upeu.edu.pe.PharmaBackend.service;
import upeu.edu.pe.PharmaBackend.dto.ClienteRequestDTO;
import upeu.edu.pe.PharmaBackend.dto.ClienteResponseDTO;
import upeu.edu.pe.PharmaBackend.service.generic.CrudService;

public interface ClienteService extends CrudService<ClienteRequestDTO, ClienteResponseDTO, Long> {
}