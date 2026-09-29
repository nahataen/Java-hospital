package com.hospital.repo;
import com.hospital.domain.ExpedServi;
import com.hospital.domain.ExpedServiId;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpedServiRepository extends JpaRepository<ExpedServi, ExpedServiId> {
  java.util.List<ExpedServi> findByExpediente(Integer expediente);
}
