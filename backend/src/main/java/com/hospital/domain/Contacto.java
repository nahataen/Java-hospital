package com.hospital.domain;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="contactos") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Contacto {
  @Id private Integer numero;
  private String nombre;
  @Column(name="apellido_paterno") private String apellidoPaterno;
  @Column(name="apellido_materno") private String apellidoMaterno;
  @Column(name="numero_telefono") private String numeroTelefono;
  @Column(name="correo_electronico") private String correoElectronico;
  @Column(name="dir_cp") private String dirCp;
  @Column(name="dir_colonia") private String dirColonia;
  @Column(name="dir_calle") private String dirCalle;
  @Column(name="dir_num_casa") private String dirNumCasa;
}
