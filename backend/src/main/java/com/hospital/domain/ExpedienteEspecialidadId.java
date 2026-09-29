package com.hospital.domain;
import java.io.Serializable;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ExpedienteEspecialidadId implements Serializable {
  private String especialidad; private Integer expediente;
}
