/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.foro.web.controller;

import com.foro.web.model.Comentario;
import com.foro.web.model.Comunidades;
import com.foro.web.model.Post;
import com.foro.web.model.Usuario;
import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.ComentarioService;
import com.foro.web.services.PostService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

   @Autowired
    private ComentarioService comentarioService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
        
    @GetMapping("/{postId}")
    public String verComentarios(@PathVariable Integer postId, Model model, 
            @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.obtenerPostPorId(postId);
        if (post == null) {
            return "redirect:/error";
        }
        
           Usuario usuario = usuarioRepository.findByUsuario(userDetails.getUsername());
    if (usuario == null) {
        return "redirect:/login";
    }
    
     // Verificar si el usuario pertenece a la comunidad del post
    Comunidades comunidadDelPost = post.getComunidad();
    if (!usuario.getComunidades().contains(comunidadDelPost)) {
 
        return "redirect:/comunidad/comunidades";
    }
    
    
    
        
        
        List<Comentario> comentarios = comentarioService.obtenerComentariosPorPostId(postId);
          if (comentarios == null) {
        comentarios = new ArrayList<>(); }
        
     
        model.addAttribute("post", post);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("postId", postId);
        
         
        if (usuario != null) {
            model.addAttribute("usuarioId", usuario.getId()); 
        } else {
            model.addAttribute("usuarioId", null); 
        }

        
        
        return "comentario";
    }

    @PostMapping("/agregar")
     public String agregarComentario(
            @RequestParam String contenido,
            @RequestParam Integer postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Post post = postService.obtenerPostPorId(postId);
        if (post == null) {
            return "redirect:/error";
        }
        
        Usuario usuario = usuarioRepository.findByUsuario(userDetails.getUsername());
        if (usuario == null) {
            return "redirect:/error";
        }
  
    Comunidades comunidadDelPost = post.getComunidad();
    if (!usuario.getComunidades().contains(comunidadDelPost)) {

        return "redirect:/comunidad/comunidades";
    }
    
    
        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setPost(post);
        comentario.setUsuario(usuario);
        comentario.setCreatedAt(LocalDateTime.now());
        
        comentarioService.agregarComentario(comentario);
        
        return "redirect:/comentarios/" + postId;
    }
}
