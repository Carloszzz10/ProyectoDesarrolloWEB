/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.foro.web.services;
import com.foro.web.model.Comentario;
import com.foro.web.model.Post;
import com.foro.web.repository.ComentarioRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public List<Comentario> obtenerComentariosPorPostId(Integer postId) {
        return comentarioRepository.findByPostId(postId);
    }

    
 public void guardarComentario(Comentario comentario) {
    comentarioRepository.save(comentario);
}

    public void agregarComentario(Comentario comentario) {
        comentarioRepository.save(comentario);
    }
    
    public long countByUsuarioId(Integer usuarioID){
        return comentarioRepository.countByUsuarioId(usuarioID);
    }
}
