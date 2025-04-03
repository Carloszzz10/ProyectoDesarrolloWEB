/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 *
 * @author Carlos Abad
 */
public class ManejoErroresLogeo implements AuthenticationFailureHandler{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        
        String errorMessage;
        
        // Agregar manejo de usuarios baneados
        if (exception instanceof UsernameNotFoundException && 
            "Usuario baneado".equals(exception.getMessage())) {
            // Redirigir a una página de usuario baneado
            response.sendRedirect("/usuario-baneado");
            return;
        }
        
        if (exception instanceof InternalAuthenticationServiceException && 
            exception.getCause() instanceof BadCredentialsException && 
            "CUENTA_INACTIVA".equals(exception.getMessage())) {
            errorMessage = "Esta cuenta se encuentra inactiva";
        } else if (exception instanceof BadCredentialsException || 
                   exception instanceof UsernameNotFoundException) {
            errorMessage = "Usuario o Contraseña inválidos";
        } else {
            errorMessage = "Error al iniciar sesión. Por favor, inténtelo de nuevo.";
        }
        
        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }
}
