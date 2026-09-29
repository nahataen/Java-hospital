package com.hospital.repo;
import com.hospital.domain.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {
}
