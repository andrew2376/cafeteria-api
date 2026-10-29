
package co.edu.autonoma.cafeteriaapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @OneToMany(
        mappedBy = "pedido",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ItemPedidoEntity> items = new ArrayList<>();

    public PedidoEntity(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        this.estado = EstadoPedido.RECIBIDO;
        this.total = BigDecimal.ZERO;
    }

    public void agregarItem(ItemPedidoEntity item) {
        items.add(item);
        item.asociarPedido(this);
    }

    public void retirarItem(ItemPedidoEntity item) {
        items.remove(item);
        item.asociarPedido(null);
        recalcularTotal();
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void recalcularTotal() {
        this.total = items.stream()
            .map(ItemPedidoEntity::calcularSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
