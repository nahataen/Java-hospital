package com.hospital.repo;
import com.hospital.domain.Tratamiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TratamientoRepository extends JpaRepository<Tratamiento, Integer> {
  List<Tratamiento> findByExpediente(Integer expediente);
}
