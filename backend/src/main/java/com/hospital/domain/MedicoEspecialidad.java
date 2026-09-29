package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="medicos_especialidad") @IdClass(MedicoEspecialidadId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MedicoEspecialidad {
  @Id private Integer medico; @Id private String especialidad;
}
