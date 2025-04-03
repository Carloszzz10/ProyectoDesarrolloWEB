package com.foro.web.repository;

import com.foro.web.model.AuditoriaPost;
import com.foro.web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaPostRepository extends JpaRepository<AuditoriaPost, Long> {
    List<AuditoriaPost> findByUsuarioQueRealizaAccion(Usuario usuario);
}
