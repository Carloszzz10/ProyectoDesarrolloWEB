/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.services;

import com.foro.web.dto.UsuarioRegistroDTO;
import com.foro.web.model.Usuario;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioServicio extends UserDetailsService {

    public Usuario GuardarUsuario(UsuarioRegistroDTO registroDTO);

    boolean confirmarCuenta(String token);

    boolean existeEmail(String email);

     public Usuario obtenerUsuarioActual();
    
}
