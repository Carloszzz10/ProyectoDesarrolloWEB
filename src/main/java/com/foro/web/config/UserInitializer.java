/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.config;

import com.foro.web.model.Rol;
import com.foro.web.model.Usuario;
import com.foro.web.repository.RolRepository;
import com.foro.web.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Carlos Abad
 */
@Component
public class UserInitializer {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @PostConstruct
    @Transactional
    public void init() {
        // Verificar o crear los roles
        Rol rolUsuario = rolRepository.findByNombres("Rol_Usuario").orElse(null);
        if (rolUsuario == null) {
            rolUsuario = new Rol();
            rolUsuario.setNombres("Rol_Usuario");
            rolUsuario = rolRepository.save(rolUsuario);
        }

        Rol rolModerador = rolRepository.findByNombres("Moderador").orElse(null);
        if (rolModerador == null) {
            rolModerador = new Rol();
            rolModerador.setNombres("Moderador");
            rolModerador = rolRepository.save(rolModerador);
        }

        Rol rolAdministrador = rolRepository.findByNombres("Administrador").orElse(null);
        if (rolAdministrador == null) {
            rolAdministrador = new Rol();
            rolAdministrador.setNombres("Administrador");
            rolAdministrador = rolRepository.save(rolAdministrador);
        }

        // Crear usuario moderador si no existe
        if (!usuarioRepository.existsByUsuario("moderador@gmail.com")) {
            Usuario moderador = new Usuario();
            moderador.setUsuario("moderador@gmail.com");
            moderador.setPassword(passwordEncoder.encode("moderador123"));
            moderador.setActivo(true);
            moderador.setNombres("Moderador");
            moderador.setApellidos("De Prueba");
            moderador.setNumero(981355809);
            moderador.setSedeEstudio("Lima");
            moderador.setImagePerfil("moderador_default.png");
            moderador.setSexo("Masculino");
            moderador.setRoles(new HashSet<>());
            moderador.getRoles().add(rolModerador);
            usuarioRepository.save(moderador);
            System.out.println("Usuario Moderador creado exitosamente");
        }

        // Crear usuario administrador si no existe
        if (!usuarioRepository.existsByUsuario("admin@gmail.com")) {
            Usuario administrador = new Usuario();
            administrador.setUsuario("admin@gmail.com");
            administrador.setPassword(passwordEncoder.encode("admin123"));
            administrador.setActivo(true);
            administrador.setNombres("Administrador");
            administrador.setApellidos("Principal");
            administrador.setNumero(999888777);
            administrador.setSedeEstudio("Arequipa");
            administrador.setImagePerfil("admin_default.png");
            administrador.setSexo("Femenino");
            administrador.setRoles(new HashSet<>());
            administrador.getRoles().add(rolAdministrador);
            usuarioRepository.save(administrador);
            System.out.println("Usuario Administrador creado exitosamente");
        }
    }
}
