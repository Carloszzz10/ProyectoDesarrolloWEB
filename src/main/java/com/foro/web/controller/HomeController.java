/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.controller;

import com.foro.web.model.Usuario;
import com.foro.web.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    
    @GetMapping("Login")
    public String login() {
        return "login";
    }

    @GetMapping("Comunidades")
    public String comunidades( Model model) {

        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();
          Usuario usuario = usuarioRepository.findByUsuario(username);
        if (usuario != null) {
            model.addAttribute("usuarioId", usuario.getId());
            model.addAttribute("imagePerfil", usuario.getImagePerfil());
        }
        return "comunidades";
    }

    @GetMapping("Publicaciones")
    public String publicacion() {
        return "publicaciones";
    }

  

    @GetMapping("Nosotros")
    public String QuienesSomos() {
        return "quienesSomos";
    }

    @GetMapping("Contactanos")
    public String contactanos() {
        return "contactanos";
    }

//    @GetMapping("Anuncios")
//    public String anuncios() {
//        return "anuncios";
//    }

    @GetMapping("Verificacion")
    public String verificaCuenta() {
        return "verificaCuenta";
    }

}

