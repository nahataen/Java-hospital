package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="pacientes_contacto") @IdClass(PacienteContactoId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PacienteContacto {
  @Id private Integer paciente; @Id private Integer contacto;
}
