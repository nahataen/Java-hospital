package com.hospital.repo;
import com.hospital.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
}
