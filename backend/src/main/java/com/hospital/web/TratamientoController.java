package com.hospital.web;

import com.hospital.domain.Tratamiento;
import com.hospital.dto.CrearTratamientoRequest;
import com.hospital.repo.TratamientoRepository;
import com.hospital.service.HospitalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {
  private final TratamientoRepository repo;
  private final HospitalService service;

  public TratamientoController(TratamientoRepository repo, HospitalService service) {
    this.repo = repo; this.service = service;
  }

  @GetMapping public List<Tratamiento> todos(@RequestParam(required = false) Integer expediente) {
    return expediente == null ? repo.findAll() : repo.findByExpediente(expediente);
  }

  @PostMapping public Tratamiento crear(@Valid @RequestBody CrearTratamientoRequest r) {
    return service.crearTratamiento(r);
  }
}
