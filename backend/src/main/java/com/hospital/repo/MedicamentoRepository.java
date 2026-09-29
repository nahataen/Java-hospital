package com.hospital.repo;
import com.hospital.domain.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicamentoRepository extends JpaRepository<Medicamento, String> {
}
