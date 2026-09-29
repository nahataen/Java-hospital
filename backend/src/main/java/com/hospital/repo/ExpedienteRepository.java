package com.hospital.repo;
import com.hospital.domain.Expediente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpedienteRepository extends JpaRepository<Expediente, Integer> {
  List<Expediente> findByPaciente(Integer paciente);
}
