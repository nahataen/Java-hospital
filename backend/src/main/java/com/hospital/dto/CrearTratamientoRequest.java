package com.hospital.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CrearTratamientoRequest {
  @NotNull private Integer expediente;
  @NotBlank private String medicamento;
  @NotBlank @Size(max = 35) private String dosis;
  @NotNull @Positive private Integer tiempoDias;
}
