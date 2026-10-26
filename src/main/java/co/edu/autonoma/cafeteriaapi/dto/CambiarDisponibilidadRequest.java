
package co.edu.autonoma.cafeteriaapi.dto;

import jakarta.validation.constraints.NotNull;

// Este DTO se utiliza para cambiar únicamente
// la disponibilidad de un producto del menú.
public record CambiarDisponibilidadRequest(

    // true significa disponible y false significa
    // que no se puede incluir en pedidos nuevos.
    @NotNull(message = "La disponibilidad es obligatoria")
    Boolean disponible

) {}
