/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.repository;

import com.foro.web.model.Comunidades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Carlos Abad
 */
@Repository
public interface ComunidadRepository extends JpaRepository<Comunidades, Integer>{

  long countByUsuariosId(Integer usuarioId);
}
