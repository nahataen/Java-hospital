package com.hospital.repo;
import com.hospital.domain.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PresentacionRepository extends JpaRepository<Presentacion, String> {
}
