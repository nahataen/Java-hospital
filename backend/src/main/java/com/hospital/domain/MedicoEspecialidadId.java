package com.hospital.domain;
import java.io.Serializable;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class MedicoEspecialidadId implements Serializable {
  private Integer medico; private String especialidad;
}
