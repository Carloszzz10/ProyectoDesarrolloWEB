/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.services;

import com.foro.web.dto.EmailDTO;
import com.foro.web.dto.UsuarioRegistroDTO;
import com.foro.web.model.Rol;
import com.foro.web.model.Usuario;
import com.foro.web.repository.RolRepository;
import com.foro.web.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Carlos Abad
 */
@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepositorio;
    private final IEmailServices emailService;
    private final RolRepository rolRepository;

    @Autowired
    public UsuarioServicioImpl(BCryptPasswordEncoder passwordEncoder, UsuarioRepository usuarioRepositorio, IEmailServices emailService, RolRepository rolRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepositorio = usuarioRepositorio;
        this.emailService = emailService;
        this.rolRepository = rolRepository;
    }

    @Override
    public Usuario GuardarUsuario(UsuarioRegistroDTO registroDTO) {
        
Rol rolUsuario = rolRepository.findByNombres("Rol_Usuario")
                         .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
      Set<Rol> roles = new HashSet<>();
    roles.add(rolUsuario);  
        Usuario usuario = new Usuario(registroDTO.getNombres(), registroDTO.getApellidos(), registroDTO.getUsuario(),
                registroDTO.getImagePerfil(), registroDTO.getNumero(), registroDTO.getSedeEstudio(), registroDTO.getSexo(),
                LocalDateTime.now(), passwordEncoder.encode(registroDTO.getPassword()),roles);

        String tokenActivacion = UUID.randomUUID().toString();
        usuario.setTokenActivacion(tokenActivacion);
        usuario.setActivo(false);

        Usuario savedUser = usuarioRepositorio.save(usuario);

        enviarCorreoVerificacion(savedUser, tokenActivacion);

        return savedUser;
        //System.out.println("Registrando usuario: " + usuario.toString());

    }

    private void enviarCorreoVerificacion(Usuario usuario, String token) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setDestinatario(usuario.getUsuario());
        emailDTO.setAsunto("Confirma tu cuenta");
        String link = "http://localhost:8080/confirmar-cuenta?token=" + token;
        emailDTO.setMensaje(link);

        emailService.enviarcorreo(emailDTO);
    }

    @Override
    public boolean confirmarCuenta(String token) {
        System.out.println("Iniciando confirmación de cuenta...");
        Usuario usuario = usuarioRepositorio.findByTokenActivacion(token);

        if (usuario != null) {
            System.out.println("Usuario encontrado con token: " + token);
            if (!usuario.getActivo()) {
                System.out.println("Activando cuenta...");
                usuario.setActivo(true);
                usuario.setTokenActivacion(null);
                usuarioRepositorio.save(usuario);
                return true;
            } else {
                System.out.println("El usuario ya está activo.");
            }
        } else {
            System.out.println("No se encontró usuario con el token proporcionado.");
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByUsuario(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("INVALID_CREDENTIALS");
        }

        if (!usuario.getActivo()) {
            System.out.println("Cuenta inactiva detectada para usuario: " + username);

            throw new BadCredentialsException("CUENTA_INACTIVA");
        }

        System.out.println("Usuario encontrado y activo: " + username);
        return new User(usuario.getUsuario(),
                usuario.getPassword(),
                MapearRoles(usuario.getRoles()));
    }

    private Collection<? extends GrantedAuthority> MapearRoles(Collection<Rol> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombres())).collect(Collectors.toList());
    }

    public boolean existeEmail(String email) {
        return usuarioRepositorio.findByUsuario(email) != null;
    }
    

    public Usuario obtenerUsuarioActual() {
        // Obtener el nombre de usuario de la sesión actual
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

       
        return usuarioRepositorio.findByUsuario(username);
    }
}
