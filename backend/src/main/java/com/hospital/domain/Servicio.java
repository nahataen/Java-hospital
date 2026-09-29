package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="servicios") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Servicio {
  @Id private Integer numero; private String nombre; private String descripcion;
}
