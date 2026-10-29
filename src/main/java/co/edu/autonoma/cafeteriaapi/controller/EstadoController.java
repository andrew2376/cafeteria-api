package co.edu.autonoma.cafeteriaapi.controller;

import co.edu.autonoma.cafeteriaapi.dto.EstadoResponse;
import co.edu.autonoma.cafeteriaapi.service.EstadoService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estado")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    public EstadoResponse consultarEstado() {
        return estadoService.consultarEstado();
    }
}