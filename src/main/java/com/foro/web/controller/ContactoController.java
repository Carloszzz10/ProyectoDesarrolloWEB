/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.foro.web.controller;

import com.foro.web.*;
import com.foro.web.model.Contacto;
import com.foro.web.model.Reporte;
import com.foro.web.model.Rol;
import com.foro.web.model.Usuario;
import com.foro.web.repository.ContactoRepository;
import com.foro.web.repository.RolRepository;
import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.ContactoService;
import com.foro.web.services.ReporteService;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ANDRES
 */
@Controller
public class ContactoController {
        @Autowired
    private ContactoService contactoService;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

       // --- Página de contacto
    @GetMapping("/contactanos")
    public String contacto(Contacto contacto, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Obtener el nombre del usuario autenticado
        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsuario(username);

        if (usuario != null) {

            model.addAttribute("usuarioId", usuario.getId());
            model.addAttribute("usuarioUsuario", usuario.getUsuario());

        }
        List<Contacto> contactos = contactoService.listaContacto();
        model.addAttribute("contactos", contactos);
        return "contacto";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Contacto contacto,
            @RequestParam("motivo") String motivo,
            @RequestParam("detalle") String detalle,
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("usuarioUsuario") String correo,
            Model model) {

        try {
            contactoService.saveContacto(contacto, usuarioId, correo);
            model.addAttribute("mensaje", "Se ha enviado un correo de verificación. Por favor, revisa tu correo y confirma antes de " +
            contacto.getFechaLimiteVerificacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
            " o será invalidado.");
        } catch (IllegalStateException e) {
            model.addAttribute("mensaje", e.getMessage());
        }

        return "contactoExitoso";
    }
    
    @PostMapping("/eliminar")
    public String eliminarContacto(@RequestParam("idContacto") Long idContacto) {
        contactoService.eliminarPorId(idContacto);
        return "redirect:/dashboard/Reports"; // Redirige al endpoint que muestra la lista actualizada
    }


    @GetMapping("/Contacto-exitoso")
    public String submitContacto(@ModelAttribute("contactoForm") Contacto contactForm, Model model) {
        model.addAttribute("successMessage", "Gracias por Contactarnos!");
        return "contactoExitoso";
    }

    @GetMapping("/verificacion")
    public String verificacionReporte(@RequestParam("id") Long id, @RequestParam("respuesta") String respuesta, Model model) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado: " + id));
        
    LocalDateTime ahora = LocalDateTime.now();

    if (contacto.getFechaLimiteVerificacion() != null && ahora.isAfter(contacto.getFechaLimiteVerificacion())) {
        model.addAttribute("mensaje", "El tiempo para verificar el reporte ha expirado.");
        return "contactoExitoso"; // Página de error o mensaje de expiración
    }
    
        if ("si".equals(respuesta)) {
            // Cambiar el estado del contacto a "confirmado" y guardar en la base de datos
            contacto.setEstado("confirmado");
            contactoRepository.save(contacto);
            model.addAttribute("mensaje", "¡Envío del reporte exitoso!");
        } else {
            model.addAttribute("mensaje", "Envío del reporte fallido.");
        }
        return "contactoExitoso";
    }
}
