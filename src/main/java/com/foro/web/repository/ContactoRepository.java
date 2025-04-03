package com.foro.web.repository;

import com.foro.web.model.Contacto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findByEstado(String estado);
    List<Contacto> findByUsuarioIdOrderByTiempoEnvioDesc(Integer usuarioId);
    Page<Contacto> findByEstado(String estado, Pageable pageable);
    Page<Contacto> findByEstadoAndMotivo(String estado, String motivo, Pageable pageable);
}
