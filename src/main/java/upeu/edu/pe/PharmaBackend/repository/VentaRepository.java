package upeu.edu.pe.PharmaBackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.PharmaBackend.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}