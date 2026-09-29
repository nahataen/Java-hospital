package com.hospital.web;

import com.hospital.domain.Paciente;
import com.hospital.dto.CrearPacienteRequest;
import com.hospital.dto.FichaPacienteDTO;
import com.hospital.repo.ExpedienteRepository;
import com.hospital.repo.PacienteRepository;
import com.hospital.service.HospitalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
  private final PacienteRepository repo;
  private final ExpedienteRepository expedientes;
  private final HospitalService service;

  public PacienteController(PacienteRepository repo, ExpedienteRepository expedientes, HospitalService service) {
    this.repo = repo; this.expedientes = expedientes; this.service = service;
  }

  @GetMapping public List<Paciente> todos() { return repo.findAll(); }

  @GetMapping("/{numero}") public Paciente uno(@PathVariable Integer numero) {
    return repo.findById(numero).orElseThrow(() -> new RecursoNoEncontrado("paciente"));
  }

  @PostMapping public Paciente crear(@Valid @RequestBody CrearPacienteRequest r) {
    return service.crearPaciente(r);
  }

  @GetMapping("/{numero}/expedientes")
  public Object expedientes(@PathVariable Integer numero) {
    uno(numero);
    return expedientes.findByPaciente(numero);
  }

  /** Ficha ligada: paciente + expedientes + tratamientos + contactos. */
  @GetMapping("/{numero}/ficha")
  public FichaPacienteDTO.Ficha ficha(@PathVariable Integer numero) {
    return service.fichaPaciente(numero);
  }

  /** Todos los tratamientos del paciente (vía sus expedientes). */
  @GetMapping("/{numero}/tratamientos")
  public List<com.hospital.domain.Tratamiento> tratamientos(@PathVariable Integer numero) {
    uno(numero);
    return service.tratamientosDePaciente(numero);
  }
}
