package com.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CrearPacienteRequest {
  @NotBlank @Size(max = 30) private String nombre;
  @Size(max = 30) private String apellidoPaterno;
  @Size(max = 30) private String apellidoMaterno;
  @NotBlank @Size(max = 15) private String numeroTelefono;
  @Size(max = 60) private String correoElectronico;
  @Pattern(regexp = "[0-9]+", message = "dirCp solo dígitos") private String dirCp;
  private String dirColonia;
  private String dirCalle;
  private String dirNumCasa;
  @NotNull private LocalDate fechaNacimiento;
}
