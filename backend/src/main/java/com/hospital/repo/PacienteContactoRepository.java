package com.hospital.repo;
import com.hospital.domain.PacienteContacto;
import com.hospital.domain.PacienteContactoId;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PacienteContactoRepository extends JpaRepository<PacienteContacto, PacienteContactoId> {
  java.util.List<PacienteContacto> findByPaciente(Integer paciente);
}
