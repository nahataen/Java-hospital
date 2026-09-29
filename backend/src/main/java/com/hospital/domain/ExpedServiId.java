package com.hospital.domain;
import java.io.Serializable;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ExpedServiId implements Serializable {
  private Integer servicio; private Integer expediente;
}
