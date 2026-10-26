
package co.edu.autonoma.cafeteriaapi.dto;

import java.math.BigDecimal;

// Este DTO define la información que la API
// devuelve cuando consultamos un producto.
public record ProductoMenuResponse(

    // Identificador generado por la base de datos.
    Long id,

    // Nombre del producto.
    String nombre,

    // Precio actual del producto.
    BigDecimal precio,

    // Indica si el producto está disponible.
    Boolean disponible

) {}
