package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="medicos") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Medico {
  @Id private Integer numero;
  @Column(name="nombre_doctor") private String nombreDoctor;
  private String apellidoP; private String apellidoM;
  @Column(name="numero_tel") private String numeroTel;
  private String rfc; private String horario;
}
