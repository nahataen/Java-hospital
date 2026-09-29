package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="tratamientos") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tratamiento {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer numero;
  private String dosis;
  @Column(name="tiempo_dias") private Integer tiempoDias;
  private Integer expediente; private String medicamento;
}
