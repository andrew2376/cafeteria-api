
package co.edu.autonoma.cafeteriaapi.repository;

import co.edu.autonoma.cafeteriaapi.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {
}
