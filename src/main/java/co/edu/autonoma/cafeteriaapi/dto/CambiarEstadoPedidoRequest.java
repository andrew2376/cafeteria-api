package co.edu.autonoma.cafeteriaapi.dto;

import co.edu.autonoma.cafeteriaapi.entity.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoPedidoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoPedido estado
) {}