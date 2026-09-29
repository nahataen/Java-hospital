package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="especialidades") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Especialidad {
  @Id private String codigo; private String nombre;
}
