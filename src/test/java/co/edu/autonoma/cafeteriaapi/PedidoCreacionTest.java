
package co.edu.autonoma.cafeteriaapi.service;

import co.edu.autonoma.cafeteriaapi.dto.PedidoRequest;
import co.edu.autonoma.cafeteriaapi.dto.PedidoResponse;
import co.edu.autonoma.cafeteriaapi.entity.PedidoEntity;
import co.edu.autonoma.cafeteriaapi.entity.ProductoMenuEntity;
import co.edu.autonoma.cafeteriaapi.repository.ItemPedidoRepository;
import co.edu.autonoma.cafeteriaapi.repository.PedidoRepository;
import co.edu.autonoma.cafeteriaapi.repository.ProductoMenuRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoCreacionTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoMenuRepository productoMenuRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void debeCrearPedidoCorrectamente() {
        ProductoMenuEntity producto = mock(ProductoMenuEntity.class);

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(producto.getDisponible()).thenReturn(true);
        when(producto.getPrecio()).thenReturn(new BigDecimal("5000"));
        when(producto.getNombre()).thenReturn("Hamburguesa");
        when(producto.getId()).thenReturn(1L);

        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(1L, 2))
        );

        PedidoResponse resultado = pedidoService.crear(request);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("10000"), resultado.total());
        assertEquals(1, resultado.items().size());

        verify(pedidoRepository).save(any(PedidoEntity.class));
    }

    @Test
    void debeRechazarCantidadCero() {
        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(1L, 0))
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.crear(request)
        );

        assertEquals(400, exception.getStatusCode().value());

        verify(productoMenuRepository, never()).findById(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarCantidadNegativa() {
        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(1L, -2))
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.crear(request)
        );

        assertEquals(400, exception.getStatusCode().value());

        verify(productoMenuRepository, never()).findById(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarCantidadNula() {
        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(1L, null))
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.crear(request)
        );

        assertEquals(400, exception.getStatusCode().value());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarProductoInexistente() {
        when(productoMenuRepository.findById(99L))
                .thenReturn(Optional.empty());

        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(99L, 2))
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.crear(request)
        );

        assertEquals(404, exception.getStatusCode().value());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarProductoNoDisponible() {
        ProductoMenuEntity producto = mock(ProductoMenuEntity.class);

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(producto.getDisponible()).thenReturn(false);
        when(producto.getNombre()).thenReturn("Hamburguesa");

        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(1L, 2))
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.crear(request)
        );

        assertEquals(400, exception.getStatusCode().value());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeRechazarPedidoSinArticulos() {
        PedidoRequest request = new PedidoRequest(List.of());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.crear(request)
        );

        assertEquals(400, exception.getStatusCode().value());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void debeCalcularCorrectamenteElTotalDeVariosProductos() {
        ProductoMenuEntity hamburguesa = mock(ProductoMenuEntity.class);
        ProductoMenuEntity gaseosa = mock(ProductoMenuEntity.class);

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(hamburguesa));

        when(productoMenuRepository.findById(2L))
                .thenReturn(Optional.of(gaseosa));

        when(hamburguesa.getDisponible()).thenReturn(true);
        when(hamburguesa.getPrecio()).thenReturn(new BigDecimal("10000"));
        when(hamburguesa.getNombre()).thenReturn("Hamburguesa");
        when(hamburguesa.getId()).thenReturn(1L);

        when(gaseosa.getDisponible()).thenReturn(true);
        when(gaseosa.getPrecio()).thenReturn(new BigDecimal("5000"));
        when(gaseosa.getNombre()).thenReturn("Gaseosa");
        when(gaseosa.getId()).thenReturn(2L);

        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PedidoRequest request = new PedidoRequest(
                List.of(
                        new PedidoRequest.ItemRequest(1L, 2),
                        new PedidoRequest.ItemRequest(2L, 3)
                )
        );

        PedidoResponse resultado = pedidoService.crear(request);

        assertEquals(new BigDecimal("35000"), resultado.total());
        assertEquals(2, resultado.items().size());
    }

    @Test
    void debeConservarElPrecioDelProductoAlCrearElPedido() {
        ProductoMenuEntity producto = mock(ProductoMenuEntity.class);

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(producto.getDisponible()).thenReturn(true);
        when(producto.getPrecio()).thenReturn(new BigDecimal("7000"));
        when(producto.getNombre()).thenReturn("Hamburguesa");
        when(producto.getId()).thenReturn(1L);

        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PedidoRequest request = new PedidoRequest(
                List.of(new PedidoRequest.ItemRequest(1L, 2))
        );

        PedidoResponse resultado = pedidoService.crear(request);

        assertEquals(new BigDecimal("14000"), resultado.total());
        assertEquals(
                new BigDecimal("7000"),
                resultado.items().get(0).precioUnitario()
        );
    }
}
