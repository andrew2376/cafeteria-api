package co.edu.autonoma.cafeteriaapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record PedidoRequest(
        @NotEmpty(message = "El pedido debe contener al menos un artículo")
        List<@Valid ItemRequest> items
) {
    public record ItemRequest(
            @NotNull(message = "El ID del producto es obligatorio")
            @Positive(message = "El ID del producto debe ser positivo")
            Long productoMenuId,

            @NotNull(message = "La cantidad es obligatoria")
            @Positive(message = "La cantidad debe ser mayor que cero")
            Integer cantidad
    ) {}
}