
package co.edu.autonoma.cafeteriaapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductoMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Boolean disponible;

    public ProductoMenuEntity(
            String nombre,
            BigDecimal precio,
            Boolean disponible) {
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
