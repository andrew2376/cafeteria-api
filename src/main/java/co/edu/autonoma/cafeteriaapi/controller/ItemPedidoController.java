
package co.edu.autonoma.cafeteriaapi.controller;

import co.edu.autonoma.cafeteriaapi.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items-pedido")
@RequiredArgsConstructor
public class ItemPedidoController {

    private final PedidoService pedidoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }
}
