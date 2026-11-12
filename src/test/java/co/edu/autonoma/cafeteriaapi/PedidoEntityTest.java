
package co.edu.autonoma.cafeteriaapi.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PedidoEntityTest {

    @Test
    void debeCrearPedidoConEstadoRecibidoYTotalCero() {
        LocalDateTime fecha = LocalDateTime.now();

        PedidoEntity pedido = new PedidoEntity(fecha);

        assertEquals(fecha, pedido.getFechaCreacion());
        assertEquals(EstadoPedido.RECIBIDO, pedido.getEstado());
        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(pedido.getTotal())
        );
        assertTrue(pedido.getItems().isEmpty());
    }

    @Test
    void debeCambiarEstadoDelPedidoCorrectamente() {
        PedidoEntity pedido = new PedidoEntity(LocalDateTime.now());

        pedido.cambiarEstado(EstadoPedido.PREPARANDO);

        assertEquals(EstadoPedido.PREPARANDO, pedido.getEstado());
    }

    @Test
    void debeRecalcularTotalDelPedidoSinArticulos() {
        PedidoEntity pedido = new PedidoEntity(LocalDateTime.now());

        pedido.recalcularTotal();

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(pedido.getTotal())
        );
    }
}
