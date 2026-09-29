package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="expedientes_especialidad") @IdClass(ExpedienteEspecialidadId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExpedienteEspecialidad {
  @Id private String especialidad; @Id private Integer expediente;
}
