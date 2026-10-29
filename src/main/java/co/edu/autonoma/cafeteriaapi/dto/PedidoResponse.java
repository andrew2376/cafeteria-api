package co.edu.autonoma.cafeteriaapi.dto;

import co.edu.autonoma.cafeteriaapi.entity.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        LocalDateTime fechaCreacion,
        EstadoPedido estado,
        BigDecimal total,
        List<ItemResponse> items
) {
    public record ItemResponse(
            Long id,
            Long productoMenuId,
            String nombreProducto,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {}
}