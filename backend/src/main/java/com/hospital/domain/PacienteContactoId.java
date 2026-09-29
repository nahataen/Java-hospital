package com.hospital.domain;
import java.io.Serializable;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class PacienteContactoId implements Serializable {
  private Integer paciente; private Integer contacto;
}
