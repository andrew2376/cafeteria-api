
package co.edu.autonoma.cafeteriaapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejar errores de validación de los DTO.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                mensaje,
                request
        );
    }

    // Manejar JSON incorrecto o mal formado.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud tiene un formato inválido.",
                request
        );
    }

    // Manejar datos inválidos y reglas de negocio incumplidas.
    @ExceptionHandler({
            IllegalArgumentException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<Map<String, Object>> manejarDatosInvalidos(
            Exception ex,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    // Manejar recursos inexistentes y errores HTTP controlados.
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresHttp(
            ResponseStatusException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.valueOf(
                ex.getStatusCode().value()
        );

        String mensaje = ex.getReason() != null
                ? ex.getReason()
                : status.getReasonPhrase();

        return construirRespuesta(status, mensaje, request);
    }

    // Manejar errores inesperados del servidor.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            Exception ex,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor.",
                request
        );
    }

    // Construir todas las respuestas con el mismo formato.
    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus status,
            String mensaje,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("status", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        respuesta.put("ruta", request.getRequestURI());

        return ResponseEntity.status(status).body(respuesta);
    }
}
