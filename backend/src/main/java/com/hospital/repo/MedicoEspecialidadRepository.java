package com.hospital.repo;
import com.hospital.domain.MedicoEspecialidad;
import com.hospital.domain.MedicoEspecialidadId;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicoEspecialidadRepository extends JpaRepository<MedicoEspecialidad, MedicoEspecialidadId> {
}
