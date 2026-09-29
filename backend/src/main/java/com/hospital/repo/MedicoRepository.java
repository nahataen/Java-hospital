package com.hospital.repo;
import com.hospital.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
}
