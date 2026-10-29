package co.edu.autonoma.cafeteriaapi.controller;

import co.edu.autonoma.cafeteriaapi.dto.CambiarDisponibilidadRequest;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuRequest;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuResponse;
import co.edu.autonoma.cafeteriaapi.service.ProductoMenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductoMenuController {

    private final ProductoMenuService productoMenuService;

    @GetMapping("/menu")
    public List<ProductoMenuResponse> obtenerMenu() {
        return productoMenuService.obtenerDisponibles();
    }

    @GetMapping("/productos-menu")
    public List<ProductoMenuResponse> obtenerTodos() {
        return productoMenuService.obtenerTodos();
    }

    @GetMapping("/productos-menu/{id}")
    public ProductoMenuResponse obtenerPorId(@PathVariable Long id) {
        return productoMenuService.obtenerPorId(id);
    }

    @PostMapping("/productos-menu")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoMenuResponse crear(
            @Valid @RequestBody ProductoMenuRequest request) {
        return productoMenuService.crear(request);
    }

    @PutMapping("/productos-menu/{id}")
    public ProductoMenuResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoMenuRequest request) {
        return productoMenuService.actualizar(id, request);
    }

    @PatchMapping("/productos-menu/{id}/disponibilidad")
    public ProductoMenuResponse cambiarDisponibilidad(
            @PathVariable Long id,
            @Valid @RequestBody CambiarDisponibilidadRequest request) {
        return productoMenuService.cambiarDisponibilidad(
                id,
                request.disponible()
        );
    }
}