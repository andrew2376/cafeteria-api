
package co.edu.autonoma.cafeteriaapi.controller;

import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuRequest;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuResponse;
import co.edu.autonoma.cafeteriaapi.dto.CambiarDisponibilidadRequest;
import co.edu.autonoma.cafeteriaapi.service.ProductoMenuService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductoMenuController {

    private final ProductoMenuService productoMenuService;

    public ProductoMenuController(
            ProductoMenuService productoMenuService) {
        this.productoMenuService = productoMenuService;
    }

    // Consultar únicamente los productos disponibles.
    @GetMapping("/menu")
    public ResponseEntity<List<ProductoMenuResponse>> consultarMenu() {
        return ResponseEntity.ok(
                productoMenuService.consultarDisponibles()
        );
    }

    // Consultar todos los productos.
    @GetMapping("/productos-menu")
    public ResponseEntity<List<ProductoMenuResponse>> consultarTodos() {
        return ResponseEntity.ok(
                productoMenuService.consultarTodos()
        );
    }

    // Consultar producto por ID.
    @GetMapping("/productos-menu/{id}")
    public ResponseEntity<ProductoMenuResponse> consultarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoMenuService.consultarPorId(id)
        );
    }

    // Registrar un producto.
    @PostMapping("/productos-menu")
    public ResponseEntity<ProductoMenuResponse> registrar(
            @Valid @RequestBody ProductoMenuRequest request) {

        ProductoMenuResponse producto =
                productoMenuService.registrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    // Actualizar un producto.
    @PutMapping("/productos-menu/{id}")
    public ResponseEntity<ProductoMenuResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoMenuRequest request) {

        return ResponseEntity.ok(
                productoMenuService.actualizar(id, request)
        );
    }

    // Cambiar disponibilidad.
    @PatchMapping("/productos-menu/{id}/disponibilidad")
    public ResponseEntity<ProductoMenuResponse> cambiarDisponibilidad(
            @PathVariable Long id,
            @Valid @RequestBody CambiarDisponibilidadRequest request) {

        return ResponseEntity.ok(
                productoMenuService.cambiarDisponibilidad(
                        id,
                        request.disponible()
                )
        );
    }

    // YUL-02: Eliminación lógica de un producto.
    @DeleteMapping("/productos-menu/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        productoMenuService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
