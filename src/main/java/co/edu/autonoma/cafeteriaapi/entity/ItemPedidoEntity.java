package co.edu.autonoma.cafeteriaapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "items_pedido")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_menu_id", nullable = false)
    private ProductoMenuEntity productoMenu;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    public ItemPedidoEntity(
            ProductoMenuEntity productoMenu,
            Integer cantidad,
            BigDecimal precioUnitario) {

        this.productoMenu = productoMenu;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public void asociarPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }

    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}