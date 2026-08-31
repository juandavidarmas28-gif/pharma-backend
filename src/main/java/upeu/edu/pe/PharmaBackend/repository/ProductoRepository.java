package upeu.edu.pe.PharmaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upeu.edu.pe.PharmaBackend.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
