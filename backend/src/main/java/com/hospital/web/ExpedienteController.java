package com.hospital.web;

import com.hospital.domain.Expediente;
import com.hospital.dto.CrearExpedienteRequest;
import com.hospital.dto.FichaPacienteDTO;
import com.hospital.repo.ExpedienteRepository;
import com.hospital.service.HospitalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expedientes")
public class ExpedienteController {
  private final ExpedienteRepository repo;
  private final HospitalService service;

  public ExpedienteController(ExpedienteRepository repo, HospitalService service) {
    this.repo = repo; this.service = service;
  }

  @GetMapping public List<Expediente> todos(@RequestParam(required = false) Integer paciente) {
    return paciente == null ? repo.findAll() : repo.findByPaciente(paciente);
  }

  @GetMapping("/{folio}") public Expediente uno(@PathVariable Integer folio) {
    return repo.findById(folio).orElseThrow(() -> new RecursoNoEncontrado("expediente"));
  }

  @PostMapping public Expediente crear(@Valid @RequestBody CrearExpedienteRequest r) {
    return service.crearExpediente(r);
  }

  @PutMapping("/{folio}/alta") public Expediente alta(@PathVariable Integer folio) {
    return service.darAlta(folio);
  }

  /** Detalle ligado: expediente + médico/habitación/servicios + tratamientos. */
  @GetMapping("/{folio}/detalle")
  public FichaPacienteDTO.ExpedienteDetalle detalle(@PathVariable Integer folio) {
    return service.detalleExpediente(folio);
  }
}
