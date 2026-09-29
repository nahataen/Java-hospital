package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="presentaciones") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Presentacion {
  @Id private String codigo; private String nombre;
  @Column(name="ViaAdministracion") private String viaAdministracion;
}
