
package co.edu.autonoma.cafeteriaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

// Este DTO recibe los datos que se necesitan
// para crear o actualizar un producto del menú.
public record ProductoMenuRequest(

    // El nombre no puede estar vacío.
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    // El precio debe ser mayor que cero.
    // BigDecimal permite trabajar con dinero con precisión.
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    BigDecimal precio,

    // La disponibilidad debe enviarse explícitamente.
    @NotNull(message = "La disponibilidad es obligatoria")
    Boolean disponible

) {}
