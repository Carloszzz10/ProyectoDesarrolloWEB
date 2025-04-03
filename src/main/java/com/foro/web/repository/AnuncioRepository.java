/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.foro.web.repository;

import com.foro.web.model.Anuncio;
import com.foro.web.model.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnuncioRepository extends JpaRepository<Anuncio, Long>{
    Page<Anuncio> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Anuncio> findByUsuarioIdOrderByCreatedAtDesc(Integer usuarioId, Pageable pageable);

    public Anuncio findTopByOrderByCreatedAtDesc();
    
    
}
