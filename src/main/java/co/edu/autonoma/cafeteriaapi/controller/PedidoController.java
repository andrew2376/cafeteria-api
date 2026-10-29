
package co.edu.autonoma.cafeteriaapi.controller;

import co.edu.autonoma.cafeteriaapi.dto.PedidoRequest;
import co.edu.autonoma.cafeteriaapi.dto.PedidoResponse;
import co.edu.autonoma.cafeteriaapi.dto.CambiarEstadoPedidoRequest;
import co.edu.autonoma.cafeteriaapi.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(
            @Valid @RequestBody PedidoRequest request) {
        return pedidoService.crear(request);
    }

    @GetMapping("/{id}")
    public PedidoResponse obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @PatchMapping("/{id}/estado")
    public PedidoResponse cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoPedidoRequest request) {
        return pedidoService.cambiarEstado(id, request.estado());
    }
}
