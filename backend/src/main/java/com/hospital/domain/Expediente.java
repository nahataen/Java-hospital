package com.hospital.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
@Entity @Table(name="expedientes") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Expediente {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer folio;
  private Integer edad; private String sintomas;
  private Double peso; private Double altura; private String diagnosticos;
  @Column(name="fecha_de_ingreso") private LocalDate fechaIngreso;
  @Column(name="numero_de_cama") private Integer numeroDeCama;
  @Column(name="fecha_de_alta") private LocalDate fechaAlta;
  private Integer paciente; private Integer habitacion; private Integer medico;
}
