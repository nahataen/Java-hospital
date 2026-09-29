package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="habitaciones") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Habitacion {
  @Id private Integer numero; private String nombre;
  @Column(name="numero_de_cama") private Integer numeroDeCama;
}
