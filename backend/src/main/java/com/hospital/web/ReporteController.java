package com.hospital.web;

import com.hospital.service.ReporteService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
  private final ReporteService reportes;

  public ReporteController(ReporteService reportes) {
    this.reportes = reportes;
  }

  @GetMapping("/ingreso") public List<Map<String, Object>> ingreso(@RequestParam Integer folio) {
    return reportes.ingreso(folio);
  }

  @GetMapping("/ingresos-paciente")
  public List<Map<String, Object>> ingresosPaciente(@RequestParam Integer paciente) {
    return reportes.ingresosPaciente(paciente);
  }

  @GetMapping("/misma-habitacion")
  public List<Map<String, Object>> mismaHabitacion(@RequestParam Integer habitacion) {
    return reportes.mismaHabitacion(habitacion);
  }

  @GetMapping("/medicos-especialidad")
  public List<Map<String, Object>> medicosEspecialidad(@RequestParam String especialidad) {
    return reportes.medicosPorEspecialidad(especialidad);
  }

  @GetMapping("/especialidad-medico")
  public List<Map<String, Object>> especialidadMedico(@RequestParam Integer medico) {
    return reportes.especialidadDeMedico(medico);
  }

  @GetMapping("/medicos-de-paciente")
  public List<Map<String, Object>> medicosDePaciente(@RequestParam Integer paciente) {
    return reportes.medicosDePaciente(paciente);
  }

  @GetMapping("/ingresos-dia")
  public List<Map<String, Object>> ingresosDia(@RequestParam String fecha) {
    if (!fecha.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fecha YYYY-MM-DD");
    return reportes.ingresosDia(fecha);
  }

  @GetMapping("/menores-activos") public List<Map<String, Object>> menoresActivos() {
    return reportes.menoresActivos();
  }

  @GetMapping("/pacientes-por-medico") public List<Map<String, Object>> pacientesPorMedico() {
    return reportes.pacientesPorMedico();
  }

  @GetMapping("/ingresos-especialidad")
  public List<Map<String, Object>> ingresosEspecialidad(
      @RequestParam(required = false) String especialidad,
      @RequestParam(required = false) String codigo) {
    if ((especialidad == null || especialidad.isBlank()) && (codigo == null || codigo.isBlank()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "especialidad o codigo requerido");
    return reportes.ingresosEspecialidad(especialidad, codigo);
  }
}
