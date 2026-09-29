package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CrearExpedienteRequest {
  @NotNull private Integer paciente;
  @NotNull private Integer habitacion;
  @NotNull private Integer medico;
  @NotNull @Size(min = 1) private List<Integer> servicios;
  @Size(max = 280) private String sintomas;
  @Size(max = 200) private String diagnosticos;
  private Double peso;
  private Double altura;
  private Integer edad;
}
