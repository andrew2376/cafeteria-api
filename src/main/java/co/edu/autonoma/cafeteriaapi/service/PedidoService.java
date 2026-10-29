package co.edu.autonoma.cafeteriaapi.service;

import co.edu.autonoma.cafeteriaapi.dto.PedidoRequest;
import co.edu.autonoma.cafeteriaapi.dto.PedidoResponse;
import co.edu.autonoma.cafeteriaapi.entity.EstadoPedido;
import co.edu.autonoma.cafeteriaapi.entity.ItemPedidoEntity;
import co.edu.autonoma.cafeteriaapi.entity.PedidoEntity;
import co.edu.autonoma.cafeteriaapi.entity.ProductoMenuEntity;
import co.edu.autonoma.cafeteriaapi.repository.PedidoRepository;
import co.edu.autonoma.cafeteriaapi.repository.ProductoMenuRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoMenuRepository productoMenuRepository;

    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        PedidoEntity pedido = new PedidoEntity(LocalDateTime.now());

        for (var itemRequest : request.items()) {
            if (itemRequest.cantidad() == null || itemRequest.cantidad() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La cantidad debe ser mayor que cero"
                );
            }

            ProductoMenuEntity producto = productoMenuRepository
                    .findById(itemRequest.productoMenuId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "No se encontró el producto con ID: "
                                    + itemRequest.productoMenuId()
                    ));

            if (!Boolean.TRUE.equals(producto.getDisponible())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El producto no está disponible: " + producto.getNombre()
                );
            }

            ItemPedidoEntity item = new ItemPedidoEntity(
                    producto,
                    itemRequest.cantidad(),
                    producto.getPrecio()
            );

            pedido.agregarItem(item);
        }

        if (pedido.getItems().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El pedido debe contener al menos un artículo"
            );
        }

        pedido.recalcularTotal();

        PedidoEntity guardado = pedidoRepository.save(pedido);
        return convertirAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtenerPorId(Long id) {
        PedidoEntity pedido = buscarPedido(id);
        return convertirAResponse(pedido);
    }

    @Transactional
    public PedidoResponse cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        PedidoEntity pedido = buscarPedido(id);

        if (nuevoEstado == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El estado es obligatorio"
            );
        }

        pedido.cambiarEstado(nuevoEstado);

        PedidoEntity actualizado = pedidoRepository.save(pedido);
        return convertirAResponse(actualizado);
    }

    private PedidoEntity buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el pedido con ID: " + id
                ));
    }

    private PedidoResponse convertirAResponse(PedidoEntity pedido) {
        List<PedidoResponse.ItemResponse> items = pedido.getItems()
                .stream()
                .map(item -> new PedidoResponse.ItemResponse(
                        item.getId(),
                        item.getProductoMenu().getId(),
                        item.getProductoMenu().getNombre(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.calcularSubtotal()
                ))
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getFechaCreacion(),
                pedido.getEstado(),
                pedido.getTotal(),
                items
        );
    }
}