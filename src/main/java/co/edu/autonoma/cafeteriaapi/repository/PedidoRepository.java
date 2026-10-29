
package co.edu.autonoma.cafeteriaapi.repository;

import co.edu.autonoma.cafeteriaapi.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
}
