package com.hospital.web;

import com.hospital.repo.*;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CatalogoController {
  private final ServicioRepository servicios;
  private final HabitacionRepository habitaciones;
  private final MedicamentoRepository medicamentos;
  private final EspecialidadRepository especialidades;
  private final MedicoRepository medicos;
  private final ContactoRepository contactos;
  private final PresentacionRepository presentaciones;
  private final ViaAdministracionRepository vias;

  public CatalogoController(ServicioRepository servicios, HabitacionRepository habitaciones,
      MedicamentoRepository medicamentos, EspecialidadRepository especialidades,
      MedicoRepository medicos, ContactoRepository contactos,
      PresentacionRepository presentaciones, ViaAdministracionRepository vias) {
    this.servicios = servicios; this.habitaciones = habitaciones;
    this.medicamentos = medicamentos; this.especialidades = especialidades;
    this.medicos = medicos; this.contactos = contactos;
    this.presentaciones = presentaciones; this.vias = vias;
  }

  @GetMapping("/servicios") public Object servicios() { return servicios.findAll(); }
  @GetMapping("/habitaciones") public Object habitaciones() { return habitaciones.findAll(); }
  @GetMapping("/medicamentos") public Object medicamentos() { return medicamentos.findAll(); }
  @GetMapping("/especialidades") public Object especialidades() { return especialidades.findAll(); }
  @GetMapping("/medicos") public Object medicos() { return medicos.findAll(); }
  @GetMapping("/contactos") public Object contactos() { return contactos.findAll(); }
  @GetMapping("/presentaciones") public Object presentaciones() { return presentaciones.findAll(); }
  @GetMapping("/vias") public Object vias() { return vias.findAll(); }

  @GetMapping("/salud") public Map<String, String> salud() { return Map.of("estado", "ok"); }
}
