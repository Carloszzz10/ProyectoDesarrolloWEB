package com.foro.web.repository;

import com.foro.web.model.Baneo;
import com.foro.web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;

@Repository
public interface BaneoRepository extends JpaRepository<Baneo, Long> {
    Optional<Baneo> findByUsuarioAndActivoTrue(Usuario usuario);
    List<Baneo> findByUsuarioAndFechaFinAfter(Usuario usuario, LocalDateTime now);
    Page<Baneo> findByActivoTrue(Pageable pageable);
    List<Baneo> findByUsuario(Usuario usuario);
}
