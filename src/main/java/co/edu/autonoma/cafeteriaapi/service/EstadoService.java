package co.edu.autonoma.cafeteriaapi.service;

import org.springframework.stereotype.Service;
import co.edu.autonoma.reservasapi.dto.EstadoResponse;

@Service
public class EstadoService {

    public EstadoResponse consultarEstado() {
        return new EstadoResponse(
                "reservas-api",
                "disponible"
        );
    }
}