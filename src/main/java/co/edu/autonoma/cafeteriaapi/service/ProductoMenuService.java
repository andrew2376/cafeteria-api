
package co.edu.autonoma.cafeteriaapi.service;

import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuRequest;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuResponse;
import co.edu.autonoma.cafeteriaapi.entity.ProductoMenuEntity;
import co.edu.autonoma.cafeteriaapi.repository.ProductoMenuRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoMenuService {

    private final ProductoMenuRepository productoMenuRepository;

    public ProductoMenuService(ProductoMenuRepository productoMenuRepository) {
        this.productoMenuRepository = productoMenuRepository;
    }

    // Consultar todos los productos.
    public List<ProductoMenuResponse> consultarTodos() {
        return productoMenuRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // Consultar únicamente los productos disponibles.
    public List<ProductoMenuResponse> consultarDisponibles() {
        return productoMenuRepository.findByDisponible(true)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // Consultar un producto por su identificador.
    public ProductoMenuResponse consultarPorId(Long id) {
        ProductoMenuEntity producto = buscarProducto(id);
        return convertirAResponse(producto);
    }

    // Registrar un producto nuevo.
    public ProductoMenuResponse registrar(ProductoMenuRequest request) {

        ProductoMenuEntity producto = new ProductoMenuEntity(
                request.nombre(),
                request.precio().doubleValue(),
                request.disponible()
        );

        ProductoMenuEntity guardado =
                productoMenuRepository.save(producto);

        return convertirAResponse(guardado);
    }

    // Actualizar los datos de un producto.
    public ProductoMenuResponse actualizar(
            Long id,
            ProductoMenuRequest request) {

        ProductoMenuEntity producto = buscarProducto(id);

        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio().doubleValue());
        producto.setDisponible(request.disponible());

        ProductoMenuEntity actualizado =
                productoMenuRepository.save(producto);

        return convertirAResponse(actualizado);
    }

    // Cambiar únicamente la disponibilidad.
    public ProductoMenuResponse cambiarDisponibilidad(
            Long id,
            Boolean disponible) {

        ProductoMenuEntity producto = buscarProducto(id);

        producto.setDisponible(disponible);

        ProductoMenuEntity actualizado =
                productoMenuRepository.save(producto);

        return convertirAResponse(actualizado);
    }

    // YUL-02: Eliminación lógica de un producto.
    public void eliminar(Long id) {

        ProductoMenuEntity producto = buscarProducto(id);

        producto.setDisponible(false);

        productoMenuRepository.save(producto);
    }

    // Buscar un producto y generar error 404 si no existe.
    private ProductoMenuEntity buscarProducto(Long id) {
        return productoMenuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"
                ));
    }

    // Convertir la entidad en el DTO de respuesta.
    private ProductoMenuResponse convertirAResponse(
            ProductoMenuEntity producto) {

        return new ProductoMenuResponse(
                producto.getId(),
                producto.getNombre(),
                BigDecimal.valueOf(producto.getPrecio()),
                producto.getDisponible()
        );
    }
}
