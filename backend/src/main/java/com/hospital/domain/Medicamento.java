package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="medicamentos") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Medicamento {
  @Id private String codigo; private String nombre;
  @Column(name="unidad_medida") private String unidadMedida;
  private String presentacion;
}
