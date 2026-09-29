package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="exped_servi") @IdClass(ExpedServiId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExpedServi {
  @Id private Integer servicio; @Id private Integer expediente;
}
