package com.hospital.dto;

import com.hospital.domain.Contacto;
import com.hospital.domain.Expediente;
import com.hospital.domain.Paciente;
import com.hospital.domain.Servicio;
import com.hospital.domain.Tratamiento;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Vista ligada: un paciente con sus expedientes, y cada expediente con sus tratamientos. */
public class FichaPacienteDTO {

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class TratamientoDetalle {
    private Tratamiento tratamiento;
    private String medicamentoNombre;
  }

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class ExpedienteDetalle {
    private Expediente expediente;
    private String medicoNombre;
    private String habitacionNombre;
    private List<Servicio> servicios;
    private List<TratamientoDetalle> tratamientos;
    private boolean activo;
  }

  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class Ficha {
    private Paciente paciente;
    private Integer edad;
    private int totalExpedientes;
    private int expedientesActivos;
    private int totalTratamientos;
    private List<ExpedienteDetalle> expedientes;
    private List<Contacto> contactos;
  }
}
