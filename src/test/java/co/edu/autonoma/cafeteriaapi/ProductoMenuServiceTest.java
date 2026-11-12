
package co.edu.autonoma.cafeteriaapi.service;
import org.springframework.test.util.ReflectionTestUtils;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuRequest;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuResponse;
import co.edu.autonoma.cafeteriaapi.entity.ProductoMenuEntity;
import co.edu.autonoma.cafeteriaapi.repository.ProductoMenuRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoMenuServiceTest {

    @Mock
    private ProductoMenuRepository productoMenuRepository;

    @InjectMocks
    private ProductoMenuService productoMenuService;

    @Test
    void debeObtenerTodosLosProductos() {
        ProductoMenuEntity producto = crearProducto(1L, "Hamburguesa", "15000", true);

        when(productoMenuRepository.findAll())
                .thenReturn(List.of(producto));

        List<ProductoMenuResponse> resultado =
                productoMenuService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Hamburguesa", resultado.get(0).nombre());
        assertEquals(new BigDecimal("15000"), resultado.get(0).precio());

        verify(productoMenuRepository).findAll();
    }

    @Test
    void debeObtenerSolamenteLosProductosDisponibles() {
        ProductoMenuEntity producto = crearProducto(1L, "Hamburguesa", "15000", true);

        when(productoMenuRepository.findByDisponible(true))
                .thenReturn(List.of(producto));

        List<ProductoMenuResponse> resultado =
                productoMenuService.obtenerDisponibles();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).disponible());

        verify(productoMenuRepository).findByDisponible(true);
    }

    @Test
    void debeObtenerProductoPorId() {
        ProductoMenuEntity producto = crearProducto(1L, "Hamburguesa", "15000", true);

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        ProductoMenuResponse resultado =
                productoMenuService.obtenerPorId(1L);

        assertEquals("Hamburguesa", resultado.nombre());
        assertEquals(1L, resultado.id());
    }

    @Test
    void debeLanzarErrorCuandoElProductoNoExiste() {
        when(productoMenuRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productoMenuService.obtenerPorId(99L)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void debeCrearProductoCorrectamente() {
        ProductoMenuRequest request = new ProductoMenuRequest(
                "Hamburguesa",
                new BigDecimal("15000"),
                true
        );

        ProductoMenuEntity guardado =
                crearProducto(1L, "Hamburguesa", "15000", true);

        when(productoMenuRepository.save(any(ProductoMenuEntity.class)))
                .thenReturn(guardado);

        ProductoMenuResponse resultado =
                productoMenuService.crear(request);

        assertEquals(1L, resultado.id());
        assertEquals("Hamburguesa", resultado.nombre());
        assertEquals(new BigDecimal("15000"), resultado.precio());
        assertTrue(resultado.disponible());

        verify(productoMenuRepository).save(any(ProductoMenuEntity.class));
    }

    @Test
    void debeActualizarProductoCorrectamente() {
        ProductoMenuEntity producto =
                crearProducto(1L, "Hamburguesa", "15000", true);

        ProductoMenuRequest request = new ProductoMenuRequest(
                "Hamburguesa especial",
                new BigDecimal("18000"),
                true
        );

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(productoMenuRepository.save(producto))
                .thenReturn(producto);

        ProductoMenuResponse resultado =
                productoMenuService.actualizar(1L, request);

        assertEquals("Hamburguesa especial", resultado.nombre());
        assertEquals(new BigDecimal("18000"), resultado.precio());
        assertTrue(resultado.disponible());

        verify(productoMenuRepository).save(producto);
    }

    @Test
    void debeCambiarDisponibilidadDelProducto() {
        ProductoMenuEntity producto =
                crearProducto(1L, "Hamburguesa", "15000", true);

        when(productoMenuRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(productoMenuRepository.save(producto))
                .thenReturn(producto);

        ProductoMenuResponse resultado =
                productoMenuService.cambiarDisponibilidad(1L, false);

        assertFalse(resultado.disponible());

        verify(productoMenuRepository).save(producto);
    }

    @Test
    void debeLanzarErrorAlActualizarProductoInexistente() {
        when(productoMenuRepository.findById(99L))
                .thenReturn(Optional.empty());

        ProductoMenuRequest request = new ProductoMenuRequest(
                "Hamburguesa",
                new BigDecimal("15000"),
                true
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productoMenuService.actualizar(99L, request)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(productoMenuRepository, never()).save(any());
    }

 private ProductoMenuEntity crearProducto(
        Long id,
        String nombre,
        String precio,
        Boolean disponible
) {
    ProductoMenuEntity producto = new ProductoMenuEntity(
            nombre,
            new BigDecimal(precio),
            disponible
    );

    ReflectionTestUtils.setField(producto, "id", id);

    return producto;
}

}
