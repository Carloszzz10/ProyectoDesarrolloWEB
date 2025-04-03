/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.controller;

import com.foro.web.repository.UsuarioRepository;
import com.foro.web.model.Usuario;
import com.foro.web.model.Baneo;

import com.foro.web.repository.BaneoRepository;
import com.foro.web.services.BaneoService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyAuthority('Moderador', 'Administrador')") //
public class BaneoController {

    @Autowired
    private BaneoService baneoService;

    @Autowired
    private BaneoRepository baneoRepository;
        @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/admin/usuarios")
    public String mostrarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        // Configurar paginación
        Pageable pageable = PageRequest.of(page, 10);
        Page<Baneo> baneos = baneoRepository.findAll(pageable);
        
        // Obtener lista de nombres de usuarios para autocompletado
        List<String> listaUsuarios = usuarioRepository.findAll()
            .stream()
            .map(Usuario::getUsuario)
            .collect(Collectors.toList());

        model.addAttribute("baneos", baneos);
        model.addAttribute("listaUsuarios", listaUsuarios);
        return "dashboard/BaneoDashboard";
    }

    @PostMapping("/admin/banear")
    public String banearUsuario(
            @RequestParam String username, 
            @RequestParam LocalDateTime fechaFin, 
            @RequestParam String razon,
            Principal principal) {
        Usuario admin = usuarioRepository.findByUsuario(principal.getName());
        
        baneoService.banearUsuario(username, fechaFin, razon, admin.getId());
        return "redirect:/admin/usuarios?baneado=true";
    }

    @PostMapping("/admin/desbanear")
    public String desbanearUsuario(@RequestParam String username) {
        baneoService.desbanearUsuario(username);
        return "redirect:/admin/usuarios?desbaneado=true";
    }
    
    @GetMapping("/usuario-baneado")
    public String mostrarPaginaBaneado() {
        return "usuario-baneado"; // Nombre de la vista
    
}
}
