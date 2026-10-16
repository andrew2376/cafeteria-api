
package co.edu.autonoma.cafeteriaapi.repository;

import co.edu.autonoma.cafeteriaapi.entity.ProductoMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoMenuRepository
        extends JpaRepository<ProductoMenuEntity, Long> {

    List<ProductoMenuEntity> findByDisponible(Boolean disponible);
}
