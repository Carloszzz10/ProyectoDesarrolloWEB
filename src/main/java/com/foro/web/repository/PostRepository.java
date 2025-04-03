/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.foro.web.repository;

import com.foro.web.model.Comunidades;
import com.foro.web.model.Post;
import com.foro.web.model.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findByUsuarioIdOrderByCreatedAtDesc(Integer usuarioId, Pageable pageable);

    long countByUsuarioId(Integer usuarioId);

    List<Post> findByComunidadId(Integer comunidadId);

    List<Post> findByComunidadIdOrderByCreatedAtDesc(Integer comunidadId);

    // Obtener posts libres (sin comunidad asignada)
    List<Post> findByComunidadIdIsNull();

    // Método para obtener los posts que pertenecen a las comunidades especificadas
    List<Post> findByComunidadIn(List<Comunidades> comunidades);

    // Método para obtener los posts que no pertenecen a ninguna comunidad (comunidad es null)
    List<Post> findByComunidadIsNull();
}
