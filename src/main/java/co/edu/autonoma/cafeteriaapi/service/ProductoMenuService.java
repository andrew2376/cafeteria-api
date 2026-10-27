
package co.edu.autonoma.cafeteriaapi.service;

import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuRequest;
import co.edu.autonoma.cafeteriaapi.dto.ProductoMenuResponse;
import co.edu.autonoma.cafeteriaapi.entity.ProductoMenuEntity;
import co.edu.autonoma.cafeteriaapi.repository.ProductoMenuRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoMenuService {

    private final ProductoMenuRepository productoMenuRepository;

    public ProductoMenuService(
            ProductoMenuRepository productoMenuRepository) {
        this.productoMenuRepository = productoMenuRepository;
    }

    // Consultar todos los productos registrados.
    @Transactional(readOnly = true)
    public List<ProductoMenuResponse> obtenerTodos() {
        return productoMenuRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // Consultar solamente los productos disponibles.
    @Transactional(readOnly = true)
    public List<ProductoMenuResponse> obtenerDisponibles() {
        return productoMenuRepository.findByDisponible(true)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // Consultar un producto por su identificador.
    @Transactional(readOnly = true)
    public ProductoMenuResponse obtenerPorId(Long id) {
        ProductoMenuEntity producto = buscarProducto(id);
        return convertirAResponse(producto);
    }

    // Registrar un producto nuevo.
    @Transactional
    public ProductoMenuResponse crear(ProductoMenuRequest request) {
        ProductoMenuEntity producto = new ProductoMenuEntity(
                request.nombre(),
                request.precio(),
                request.disponible()
        );

        ProductoMenuEntity guardado =
                productoMenuRepository.save(producto);

        return convertirAResponse(guardado);
    }

    // Actualizar todos los datos de un producto.
    @Transactional
    public ProductoMenuResponse actualizar(
            Long id,
            ProductoMenuRequest request) {

        ProductoMenuEntity producto = buscarProducto(id);

        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setDisponible(request.disponible());

        ProductoMenuEntity actualizado =
                productoMenuRepository.save(producto);

        return convertirAResponse(actualizado);
    }

    // Cambiar únicamente la disponibilidad.
    @Transactional
    public ProductoMenuResponse cambiarDisponibilidad(
            Long id,
            Boolean disponible) {

        ProductoMenuEntity producto = buscarProducto(id);

        producto.setDisponible(disponible);

        ProductoMenuEntity actualizado =
                productoMenuRepository.save(producto);

        return convertirAResponse(actualizado);
    }

    // Buscar un producto o devolver un error 404.
    private ProductoMenuEntity buscarProducto(Long id) {
        return productoMenuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el producto con ID: " + id
                ));
    }

    // Convertir la entidad en el DTO de respuesta.
    private ProductoMenuResponse convertirAResponse(
            ProductoMenuEntity producto) {

        return new ProductoMenuResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDisponible()
        );
    }
}
