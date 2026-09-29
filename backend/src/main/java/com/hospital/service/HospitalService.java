package com.hospital.service;

import com.hospital.domain.*;
import com.hospital.dto.*;
import com.hospital.repo.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HospitalService {
  private final PacienteRepository pacientes;
  private final ExpedienteRepository expedientes;
  private final ExpedServiRepository expedServi;
  private final ExpedienteEspecialidadRepository expedEsp;
  private final TratamientoRepository tratamientos;
  private final HabitacionRepository habitaciones;
  private final MedicamentoRepository medicamentos;
  private final MedicoRepository medicos;
  private final ServicioRepository servicios;
  private final PacienteContactoRepository pacienteContactos;
  private final ContactoRepository contactos;

  public HospitalService(PacienteRepository pacientes, ExpedienteRepository expedientes,
      ExpedServiRepository expedServi, ExpedienteEspecialidadRepository expedEsp,
      TratamientoRepository tratamientos, HabitacionRepository habitaciones,
      MedicamentoRepository medicamentos, MedicoRepository medicos,
      ServicioRepository servicios, PacienteContactoRepository pacienteContactos,
      ContactoRepository contactos) {
    this.pacientes = pacientes; this.expedientes = expedientes;
    this.expedServi = expedServi; this.expedEsp = expedEsp;
    this.tratamientos = tratamientos; this.habitaciones = habitaciones;
    this.medicamentos = medicamentos; this.medicos = medicos;
    this.servicios = servicios; this.pacienteContactos = pacienteContactos;
    this.contactos = contactos;
  }

  /** Equivale a Menu:1. numero manual MAX+1 como en MetodosPacienteInsert. */
  @Transactional
  public Paciente crearPaciente(CrearPacienteRequest r) {
    Paciente p = new Paciente();
    p.setNumero(pacientes.findAll().stream().mapToInt(Paciente::getNumero).max().orElse(0) + 1);
    p.setNombre(r.getNombre());
    p.setApellidoPaterno(emptyToNull(r.getApellidoPaterno()));
    p.setApellidoMaterno(emptyToNull(r.getApellidoMaterno()));
    p.setNumeroTelefono(r.getNumeroTelefono());
    p.setCorreoElectronico(emptyToNull(r.getCorreoElectronico()));
    p.setDirCp(r.getDirCp()); p.setDirColonia(r.getDirColonia());
    p.setDirCalle(r.getDirCalle()); p.setDirNumCasa(r.getDirNumCasa());
    p.setFechaNacimiento(r.getFechaNacimiento());
    return pacientes.save(p);
  }

  /**
   * Equivale a Menu:2 (MetodoGenerarExpediente + ExpedientesInsert).
   * Corrige al CLI: folio AUTO_INCREMENT (no MAX+1 con carrera) y no usa
   * el id de servicio como id de médico para nada más que el expediente.
   */
  @Transactional
  public Expediente crearExpediente(CrearExpedienteRequest r) {
    Paciente p = pacientes.findById(r.getPaciente())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "paciente inexistente"));
    Habitacion h = habitaciones.findById(r.getHabitacion())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "habitacion inexistente"));
    if (h.getNumeroDeCama() == null || h.getNumeroDeCama() < 1 || h.getNumeroDeCama() > 3)
      throw new ResponseStatusException(HttpStatus.CONFLICT, "habitacion sin camas disponibles (1..3)");
    h.setNumeroDeCama(h.getNumeroDeCama() - 1);
    habitaciones.save(h);

    Expediente e = new Expediente();
    e.setPaciente(p.getNumero());
    e.setHabitacion(h.getNumero());
    e.setMedico(r.getMedico());
    e.setSintomas(r.getSintomas());
    e.setDiagnosticos(r.getDiagnosticos());
    e.setPeso(r.getPeso()); e.setAltura(r.getAltura());
    e.setFechaIngreso(LocalDate.now());
    e.setNumeroDeCama(h.getNumeroDeCama());
    e.setFechaAlta(null);
    int edad = r.getEdad() != null ? r.getEdad()
        : Period.between(p.getFechaNacimiento(), LocalDate.now()).getYears();
    e.setEdad(edad);
    e = expedientes.save(e);

    for (Integer s : r.getServicios()) {
      expedServi.save(new ExpedServi(s, e.getFolio()));
      // Mapeo CLI: servicio 1..9 -> ESP01..ESP09, 10 -> ESP10
      if (s >= 1 && s <= 10) {
        String esp = s == 10 ? "ESP10" : "ESP0" + s;
        expedEsp.save(new ExpedienteEspecialidad(esp, e.getFolio()));
      }
    }
    return e;
  }

  /** Equivale a Menu:3 (Tratamiento.MetodoGenTratamiento). numero AUTO_INCREMENT. */
  @Transactional
  public Tratamiento crearTratamiento(CrearTratamientoRequest r) {
    if (!expedientes.existsById(r.getExpediente()))
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "expediente inexistente");
    if (!medicamentos.existsById(r.getMedicamento()))
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "medicamento inexistente");
    Tratamiento t = new Tratamiento();
    t.setExpediente(r.getExpediente());
    t.setMedicamento(r.getMedicamento());
    t.setDosis(r.getDosis());
    t.setTiempoDias(r.getTiempoDias());
    return tratamientos.save(t);
  }

  /**
   * Equivale a Menu:4 (update.alta) pero corregido: filtra por el folio
   * pedido (el CLI hardcodeaba folio=1) y sí libera la cama con UPDATE.
   */
  @Transactional
  public Expediente darAlta(Integer folio) {
    Expediente e = expedientes.findById(folio)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "folio inexistente"));
    if (e.getFechaAlta() != null)
      throw new ResponseStatusException(HttpStatus.CONFLICT, "folio ya dado de alta");
    e.setFechaAlta(LocalDate.now());
    expedientes.save(e);
    habitaciones.findById(e.getHabitacion()).ifPresent(h -> {
      h.setNumeroDeCama((h.getNumeroDeCama() == null ? 0 : h.getNumeroDeCama()) + 1);
      habitaciones.save(h);
    });
    return e;
  }

  public List<Tratamiento> tratamientosDe(Integer folio) {
    return tratamientos.findByExpediente(folio);
  }

  /** Todos los tratamientos de un paciente (a través de sus expedientes). */
  @Transactional(readOnly = true)
  public List<Tratamiento> tratamientosDePaciente(Integer numero) {
    List<Integer> folios = expedientes.findByPaciente(numero).stream()
        .map(Expediente::getFolio).toList();
    return folios.stream().flatMap(f -> tratamientos.findByExpediente(f).stream()).toList();
  }

  /**
   * Ficha ligada: paciente + cada expediente con médico/habitación/servicios
   * + tratamientos de cada expediente + contactos. Es lo que usa la UI para
   * mostrar "un paciente con su expediente, tratamiento y todos esos datos".
   */
  @Transactional(readOnly = true)
  public FichaPacienteDTO.Ficha fichaPaciente(Integer numero) {
    Paciente p = pacientes.findById(numero)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "paciente inexistente"));
    List<Expediente> exps = expedientes.findByPaciente(numero);
    List<FichaPacienteDTO.ExpedienteDetalle> detalles =
        exps.stream().map(this::armarDetalle).toList();
    int activos = (int) detalles.stream().filter(FichaPacienteDTO.ExpedienteDetalle::isActivo).count();
    int totalTrat = detalles.stream().mapToInt(d -> d.getTratamientos().size()).sum();
    List<Contacto> conts = pacienteContactos.findByPaciente(numero).stream()
        .map(pc -> contactos.findById(pc.getContacto()).orElse(null))
        .filter(c -> c != null).toList();
    Integer edad = null;
    try {
      if (p.getFechaNacimiento() != null)
        edad = Period.between(p.getFechaNacimiento(), LocalDate.now()).getYears();
    } catch (Exception ignored) { }
    return new FichaPacienteDTO.Ficha(p, edad, detalles.size(), activos, totalTrat, detalles, conts);
  }

  /** Detalle ligado de un expediente: expediente + paciente + tratamientos con nombre de medicamento. */
  @Transactional(readOnly = true)
  public FichaPacienteDTO.ExpedienteDetalle detalleExpediente(Integer folio) {
    Expediente e = expedientes.findById(folio)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "folio inexistente"));
    return armarDetalle(e);
  }

  private FichaPacienteDTO.ExpedienteDetalle armarDetalle(Expediente e) {
    String medicoNombre = medicos.findById(e.getMedico())
        .map(m -> join(" ", m.getNombreDoctor(), m.getApellidoP(), m.getApellidoM())).orElse("—");
    String habNombre = habitaciones.findById(e.getHabitacion())
        .map(h -> h.getNumero() + " - " + h.getNombre()).orElse("—");
    List<Servicio> srvs = expedServi.findByExpediente(e.getFolio()).stream()
        .map(es -> servicios.findById(es.getServicio()).orElse(null))
        .filter(s -> s != null).toList();
    List<FichaPacienteDTO.TratamientoDetalle> trats = tratamientos.findByExpediente(e.getFolio()).stream()
        .map(t -> new FichaPacienteDTO.TratamientoDetalle(t,
            medicamentos.findById(t.getMedicamento()).map(Medicamento::getNombre).orElse(t.getMedicamento())))
        .toList();
    return new FichaPacienteDTO.ExpedienteDetalle(e, medicoNombre, habNombre, srvs, trats, e.getFechaAlta() == null);
  }

  private static String join(String sep, String... parts) {
    StringBuilder sb = new StringBuilder();
    for (String s : parts) {
      if (s == null || s.isBlank()) continue;
      if (!sb.isEmpty()) sb.append(sep);
      sb.append(s.strip());
    }
    return sb.isEmpty() ? "—" : sb.toString();
  }

  private static String emptyToNull(String s) {
    return s == null || s.isBlank() ? null : s;
  }
}
