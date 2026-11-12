
package co.edu.autonoma.cafeteriaapi.service;

import co.edu.autonoma.cafeteriaapi.dto.PedidoResponse;
import co.edu.autonoma.cafeteriaapi.entity.EstadoPedido;
import co.edu.autonoma.cafeteriaapi.entity.ItemPedidoEntity;
import co.edu.autonoma.cafeteriaapi.entity.PedidoEntity;
import co.edu.autonoma.cafeteriaapi.repository.ItemPedidoRepository;
import co.edu.autonoma.cafeteriaapi.repository.PedidoRepository;
import co.edu.autonoma.cafeteriaapi.repository.ProductoMenuRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoMenuRepository productoMenuRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    // TRANSICIONES DE ESTADO

    @Test
    void debePermitirTransicionDeRecibidoAPreparando() {
        PedidoEntity pedido = mock(PedidoEntity.class);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedido.getEstado()).thenReturn(EstadoPedido.RECIBIDO);
        when(pedido.getItems()).thenReturn(List.of());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        assertDoesNotThrow(() ->
                pedidoService.cambiarEstado(1L, EstadoPedido.PREPARANDO));

        verify(pedido).cambiarEstado(EstadoPedido.PREPARANDO);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void debePermitirTransicionDePreparandoAListo() {
        PedidoEntity pedido = mock(PedidoEntity.class);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedido.getEstado()).thenReturn(EstadoPedido.PREPARANDO);
        when(pedido.getItems()).thenReturn(List.of());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        assertDoesNotThrow(() ->
                pedidoService.cambiarEstado(1L, EstadoPedido.LISTO));

        verify(pedido).cambiarEstado(EstadoPedido.LISTO);
    }

    @Test
    void debePermitirTransicionDeListoAEntregado() {
        PedidoEntity pedido = mock(PedidoEntity.class);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedido.getEstado()).thenReturn(EstadoPedido.LISTO);
        when(pedido.getItems()).thenReturn(List.of());
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        assertDoesNotThrow(() ->
                pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO));

        verify(pedido).cambiarEstado(EstadoPedido.ENTREGADO);
    }

    @Test
    void debeRechazarTransicionNoPermitida() {
        PedidoEntity pedido = mock(PedidoEntity.class);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedido.getEstado()).thenReturn(EstadoPedido.RECIBIDO);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarEstadoNulo() {
        PedidoEntity pedido = mock(PedidoEntity.class);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.cambiarEstado(1L, null)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(pedidoRepository, never()).save(any());
    }

    // ELIMINACIÓN DE ÍTEMS

    @Test
    void debePermitirEliminarItemDePedidoRecibido() {
        PedidoEntity pedido = mock(PedidoEntity.class);
        ItemPedidoEntity item = mock(ItemPedidoEntity.class);

        when(itemPedidoRepository.findById(10L))
                .thenReturn(Optional.of(item));

        when(item.getPedido()).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(EstadoPedido.RECIBIDO);
        when(pedido.getItems()).thenReturn(List.of(
                mock(ItemPedidoEntity.class),
                item
        ));

        pedidoService.eliminarItem(10L);

        verify(pedido).retirarItem(item);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void debeRechazarEliminarItemSiPedidoEstaPreparando() {
        PedidoEntity pedido = mock(PedidoEntity.class);
        ItemPedidoEntity item = mock(ItemPedidoEntity.class);

        when(itemPedidoRepository.findById(10L))
                .thenReturn(Optional.of(item));

        when(item.getPedido()).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(EstadoPedido.PREPARANDO);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.eliminarItem(10L)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(pedido, never()).retirarItem(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarEliminarElUltimoItem() {
        PedidoEntity pedido = mock(PedidoEntity.class);
        ItemPedidoEntity item = mock(ItemPedidoEntity.class);

        when(itemPedidoRepository.findById(10L))
                .thenReturn(Optional.of(item));

        when(item.getPedido()).thenReturn(pedido);
        when(pedido.getEstado()).thenReturn(EstadoPedido.RECIBIDO);
        when(pedido.getItems()).thenReturn(List.of(item));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.eliminarItem(10L)
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(pedido, never()).retirarItem(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRetornarErrorSiElItemNoExiste() {
        when(itemPedidoRepository.findById(10L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.eliminarItem(10L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(pedidoRepository, never()).save(any());
    }

    // CONSULTA DE PEDIDOS POR ID

    @Test
    void debeObtenerPedidoPorIdCorrectamente() {
        Long id = 1L;

        PedidoEntity pedido = mock(PedidoEntity.class);

        when(pedidoRepository.findById(id))
                .thenReturn(Optional.of(pedido));

        when(pedido.getId()).thenReturn(id);
        when(pedido.getFechaCreacion()).thenReturn(LocalDateTime.now());
        when(pedido.getEstado()).thenReturn(EstadoPedido.RECIBIDO);
        when(pedido.getTotal()).thenReturn(new BigDecimal("15000"));
        when(pedido.getItems()).thenReturn(List.of());

        PedidoResponse response = pedidoService.obtenerPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(EstadoPedido.RECIBIDO, response.estado());
        assertEquals(
                0,
                new BigDecimal("15000").compareTo(response.total())
        );
        assertTrue(response.items().isEmpty());

        verify(pedidoRepository).findById(id);
    }

    @Test
    void debeRetornarErrorCuandoElPedidoNoExiste() {
        Long id = 999L;

        when(pedidoRepository.findById(id))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.obtenerPorId(id)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(pedidoRepository).findById(id);
        verify(pedidoRepository, never()).save(any());
    }
}
