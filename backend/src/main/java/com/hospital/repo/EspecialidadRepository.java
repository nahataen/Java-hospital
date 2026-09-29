package com.hospital.repo;
import com.hospital.domain.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EspecialidadRepository extends JpaRepository<Especialidad, String> {
}
