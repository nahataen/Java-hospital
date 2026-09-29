package com.hospital.repo;
import com.hospital.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
}
