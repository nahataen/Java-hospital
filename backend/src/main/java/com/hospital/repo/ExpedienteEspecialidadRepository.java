package com.hospital.repo;
import com.hospital.domain.ExpedienteEspecialidad;
import com.hospital.domain.ExpedienteEspecialidadId;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpedienteEspecialidadRepository extends JpaRepository<ExpedienteEspecialidad, ExpedienteEspecialidadId> {
}
