package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="ViaAdministraciones") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ViaAdministracion {
  @Id private String codigo; private String descripcion;
}
