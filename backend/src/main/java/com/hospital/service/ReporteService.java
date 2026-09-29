package com.hospital.service;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Los 10 reportes del CLI (Cons1-Cons10 / QUERYS.sql Q1-Q10) como SQL
 * corregido: LEFT JOIN a tratamientos/medicamentos para que los
 * expedientes SIN tratamiento también aparezcan (el CLI los perdía por
 * usar INNER JOIN en la rama "sin tratamiento", además rota).
 */
@Service
public class ReporteService {
  private final JdbcTemplate jdbc;

  public ReporteService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private static String nombre(String alias) {
    return "CONCAT(IFNULL(" + alias + ".nombre,''),' ',IFNULL(" + alias + ".apellido_paterno,''),' ',IFNULL(" + alias + ".apellido_materno,''))";
  }

  private static String nombreMedico(String alias) {
    return "CONCAT(IFNULL(" + alias + ".nombre_doctor,''),' ',IFNULL(" + alias + ".apellidoP,''),' ',IFNULL(" + alias + ".apellidoM,''))";
  }

  /** Cons1: información de un ingreso/folio. */
  public List<Map<String, Object>> ingreso(Integer folio) {
    return jdbc.queryForList("""
        SELECT p.numero AS paciente, %s AS paciente_nombre,
          DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          e.habitacion, e.numero_de_cama AS camas, %s AS medico,
          e.sintomas, e.diagnosticos, es.nombre AS especialidad,
          md.nombre AS medicamento, t.dosis, t.tiempo_dias AS dias
        FROM expedientes e
        JOIN pacientes p ON p.numero=e.paciente
        JOIN medicos m ON m.numero=e.medico
        LEFT JOIN expedientes_especialidad ee ON ee.expediente=e.folio
        LEFT JOIN especialidades es ON es.codigo=ee.especialidad
        LEFT JOIN tratamientos t ON t.expediente=e.folio
        LEFT JOIN medicamentos md ON md.codigo=t.medicamento
        WHERE e.folio=?""".formatted(nombre("p"), nombreMedico("m")), folio);
  }

  /** Cons2: ingresos que ha tenido un paciente. */
  public List<Map<String, Object>> ingresosPaciente(Integer paciente) {
    return jdbc.queryForList("""
        SELECT %s AS paciente_nombre, e.folio AS codigo,
          DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          %s AS medico, es.nombre AS especialidad,
          e.sintomas, e.diagnosticos, MAX(t.tiempo_dias) AS dias_hospitalizacion
        FROM expedientes e
        JOIN pacientes p ON p.numero=e.paciente
        JOIN medicos m ON m.numero=e.medico
        LEFT JOIN expedientes_especialidad ee ON ee.expediente=e.folio
        LEFT JOIN especialidades es ON es.codigo=ee.especialidad
        LEFT JOIN tratamientos t ON t.expediente=e.folio
        WHERE p.numero=? GROUP BY e.folio""".formatted(nombre("p"), nombreMedico("m")), paciente);
  }

  /** Cons3: pacientes en la misma habitación. */
  public List<Map<String, Object>> mismaHabitacion(Integer habitacion) {
    return jdbc.queryForList("""
        SELECT e.folio, %s AS paciente,
          DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          e.diagnosticos, h.numero AS habitacion, e.numero_de_cama AS camas
        FROM expedientes e
        JOIN pacientes p ON p.numero=e.paciente
        JOIN habitaciones h ON h.numero=e.habitacion
        WHERE h.numero=?""".formatted(nombre("p")), habitacion);
  }

  /** Cons4: médicos con la misma especialidad (por nombre). */
  public List<Map<String, Object>> medicosPorEspecialidad(String especialidad) {
    return jdbc.queryForList("""
        SELECT %s AS medico, es.nombre AS especialidad
        FROM medicos m
        JOIN medicos_especialidad me ON me.medico=m.numero
        JOIN especialidades es ON es.codigo=me.especialidad
        WHERE es.nombre=?""".formatted(nombreMedico("m")), especialidad);
  }

  /** Cons5: especialidad(es) de un médico. */
  public List<Map<String, Object>> especialidadDeMedico(Integer medico) {
    return jdbc.queryForList("""
        SELECT %s AS medico, es.nombre AS especialidad
        FROM medicos m
        JOIN medicos_especialidad me ON me.medico=m.numero
        JOIN especialidades es ON es.codigo=me.especialidad
        WHERE m.numero=?""".formatted(nombreMedico("m")), medico);
  }

  /** Cons6: médicos que han atendido a un paciente. */
  public List<Map<String, Object>> medicosDePaciente(Integer paciente) {
    return jdbc.queryForList("""
        SELECT %s AS paciente, e.folio, e.sintomas, e.diagnosticos,
          es.nombre AS especialidad, %s AS medico,
          DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          IFNULL(DATE_FORMAT(e.fecha_de_alta,'%%d-%%m-%%y'),' ') AS fecha_alta
        FROM expedientes e
        JOIN pacientes p ON p.numero=e.paciente
        JOIN medicos m ON m.numero=e.medico
        LEFT JOIN expedientes_especialidad ee ON ee.expediente=e.folio
        LEFT JOIN especialidades es ON es.codigo=ee.especialidad
        WHERE p.numero=?""".formatted(nombre("p"), nombreMedico("m")), paciente);
  }

  /** Cons7: pacientes que ingresaron el mismo día (YYYY-MM-DD). */
  public List<Map<String, Object>> ingresosDia(String fecha) {
    return jdbc.queryForList("""
        SELECT e.folio,
          DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          %s AS paciente, e.diagnosticos,
          h.numero AS habitacion, e.numero_de_cama AS camas,
          %s AS medico, es.nombre AS especialidad
        FROM expedientes e
        JOIN pacientes p ON p.numero=e.paciente
        JOIN habitaciones h ON h.numero=e.habitacion
        JOIN medicos m ON m.numero=e.medico
        LEFT JOIN expedientes_especialidad ee ON ee.expediente=e.folio
        LEFT JOIN especialidades es ON es.codigo=ee.especialidad
        WHERE e.fecha_de_ingreso=?""".formatted(nombre("p"), nombreMedico("m")), fecha);
  }

  /** Cons8: niños <13 hospitalizados actualmente. */
  public List<Map<String, Object>> menoresActivos() {
    return jdbc.queryForList("""
        SELECT e.folio, %s AS paciente, p.fecha_de_nacimiento,
          e.edad, DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          e.sintomas, e.diagnosticos, %s AS medico, es.nombre AS especialidad
        FROM expedientes e
        JOIN pacientes p ON p.numero=e.paciente
        JOIN medicos m ON m.numero=e.medico
        LEFT JOIN expedientes_especialidad ee ON ee.expediente=e.folio
        LEFT JOIN especialidades es ON es.codigo=ee.especialidad
        WHERE e.edad<13 AND e.fecha_de_alta IS NULL""".formatted(nombre("p"), nombreMedico("m")));
  }

  /** Cons9: cantidad de ingresos atendidos por médico. */
  public List<Map<String, Object>> pacientesPorMedico() {
    return jdbc.queryForList("""
        SELECT %s AS medico, COUNT(e.paciente) AS pacientes_atendidos
        FROM medicos m INNER JOIN expedientes e ON m.numero=e.medico
        GROUP BY m.numero""".formatted(nombreMedico("m")));
  }

  /** Cons10: ingresos por especialidad (nombre o código ESPxx). */
  public List<Map<String, Object>> ingresosEspecialidad(String especialidad, String codigo) {
    if (codigo != null && !codigo.isBlank()) {
      return jdbc.queryForList("""
          SELECT es.nombre AS especialidad, %s AS medico, e.folio,
            %s AS paciente,
            DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
            e.sintomas, e.diagnosticos
          FROM expedientes e
          JOIN expedientes_especialidad ee ON ee.expediente=e.folio
          JOIN especialidades es ON es.codigo=ee.especialidad
          JOIN medicos m ON m.numero=e.medico
          JOIN pacientes p ON p.numero=e.paciente
          WHERE es.codigo=?""".formatted(nombreMedico("m"), nombre("p")), codigo);
    }
    return jdbc.queryForList("""
        SELECT es.nombre AS especialidad, %s AS medico, e.folio,
          %s AS paciente,
          DATE_FORMAT(e.fecha_de_ingreso,'%%d-%%m-%%y') AS fecha_ingreso,
          e.sintomas, e.diagnosticos
        FROM expedientes e
        JOIN expedientes_especialidad ee ON ee.expediente=e.folio
        JOIN especialidades es ON es.codigo=ee.especialidad
        JOIN medicos m ON m.numero=e.medico
        JOIN pacientes p ON p.numero=e.paciente
        WHERE es.nombre=?""".formatted(nombreMedico("m"), nombre("p")), especialidad);
  }
}
