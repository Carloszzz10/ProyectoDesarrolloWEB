/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.services;

import com.foro.web.model.Comunidades;
import com.foro.web.model.Usuario;
import com.foro.web.repository.ComunidadRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Carlos Abad
 */

@Service
public class ComunidadService {

    @Autowired
    private ComunidadRepository comunidadRepository;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    
        public Comunidades crearComunidad(Comunidades comunidad) {
        return comunidadRepository.save(comunidad);
    }
        
        
        public List<Comunidades> obtenerTodasLasComunidades() {
        return comunidadRepository.findAll();
    }
        
        
        
    public Comunidades obtenerComunidadPorId(Integer comunidadId) {
        return comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
    }    
        
      public Comunidades getComunidadById(Integer id) {
        return comunidadRepository.findById(id).orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
    }
      // Método para unirse a una comunidad
    public void unirseAComunidad(Integer comunidadId) {
        Comunidades comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        
        // Obtener el usuario actual
        Usuario usuario = usuarioServicio.obtenerUsuarioActual();
        
        if (usuario != null) {
            // Agregar al usuario a la comunidad
            comunidad.getUsuarios().add(usuario);
            comunidadRepository.save(comunidad); // Guardar la comunidad con el usuario agregado
        }
    }  
        
// Método para salir de una comunidad
public void salirDeComunidad(Integer comunidadId) {
    Comunidades comunidad = comunidadRepository.findById(comunidadId)
            .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

    // Obtener el usuario actual
    Usuario usuario = usuarioServicio.obtenerUsuarioActual();

    if (usuario != null) {
        // Remover al usuario de la comunidad
        comunidad.getUsuarios().remove(usuario);
        comunidadRepository.save(comunidad); // Guardar la comunidad con el usuario eliminado
    }
}
     public long countByUsuarioId(Integer usuarioId) {
        return comunidadRepository.countByUsuariosId(usuarioId);
    }
 
}
