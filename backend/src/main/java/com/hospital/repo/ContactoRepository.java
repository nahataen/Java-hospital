package com.hospital.repo;
import com.hospital.domain.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
}
