/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.controller;

import com.foro.web.*;
import com.foro.web.model.Baneo;
import com.foro.web.model.Contacto;
import com.foro.web.model.Reporte;
import com.foro.web.model.Rol;
import com.foro.web.model.Usuario;
import com.foro.web.repository.BaneoRepository;
import com.foro.web.repository.ContactoRepository;
import com.foro.web.repository.RolRepository;
import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.BaneoService;
import com.foro.web.services.ContactoService;
import com.foro.web.services.ReporteService;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * @author Carlos Abad
 */
@Controller
public class SesionController {

    @Autowired
    private BaneoService baneoService;

    @Autowired
    private BaneoRepository baneoRepository;
    
    @Autowired
    private ContactoService contactoService;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // --- Login
    @GetMapping("/login")
    public String iniciarSesion() {
        return "login";
    }

    // --- Dashboard principal
    @GetMapping("/dashboard")
    public String dashboard(Model model,@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Baneo> baneos = baneoRepository.findAll(pageable);
        
        // Obtener lista de nombres de usuarios para autocompletado
        List<String> listaUsuarios = usuarioRepository.findAll()
            .stream()
            .map(Usuario::getUsuario)
            .collect(Collectors.toList());

        model.addAttribute("baneos", baneos);
        model.addAttribute("listaUsuarios", listaUsuarios);
        return "dashboard";
    }

    // --- Listado de contactos con paginación
    @GetMapping("dashboard/Reports")
    public String contactodashboard(Model model, 
                                    @RequestParam(defaultValue = "0") int page, 
                                    @RequestParam(defaultValue = "") String motivo) {
       int pageSize = 5;
       String estado = "confirmado"; // Filtrar solo contactos confirmados

       // Si se ha proporcionado un motivo, filtra por motivo además del estado
       Page<Contacto> contactos;
       if (motivo.isEmpty()) {
           contactos = contactoService.listaContactoPaginadoPorEstado(estado, page, pageSize);
       } else {
           // Si hay motivo, lo filtra junto con el estado
           contactos = contactoService.listaContactoPaginadoPorEstadoYMotivo(estado, motivo, page, pageSize);
       }

       model.addAttribute("contactos", contactos);
       model.addAttribute("currentPage", page);
       model.addAttribute("totalPages", contactos.getTotalPages());
       model.addAttribute("motivoSeleccionado", motivo);  // Para mantener el valor del filtro en el formulario
       return "dashboard/contactoDashboard";
   }    

    // --- Asignación de Roles
    @GetMapping("dashboard/asignar-rol")
    public String mostrarFormularioAsignacionRol(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("roles", rolRepository.findAll());
        return "dashboard/AsignarRolDashboard";
    }

    @PostMapping("dashboard/asignar-rol")
    public String asignarRol(@RequestParam Integer usuarioId, @RequestParam Integer rolId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Rol rol = rolRepository.findById(rolId).orElse(null);

        if (usuario != null && rol != null) {
            // Validación: agregar el rol solo si el usuario no lo tiene
            if (!usuario.getRoles().contains(rol)) {
                usuario.getRoles().add(rol);
                usuarioRepository.save(usuario);
            }
        }

        return "redirect:/dashboard/asignar-rol";
    }

    @PostMapping("/dashboard/eliminar-rol")
    public String eliminarRol(@RequestParam Integer usuarioId, @RequestParam Integer rolId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Rol rol = rolRepository.findById(rolId).orElse(null);

        if (usuario != null && rol != null) {
            usuario.getRoles().remove(rol);
            usuarioRepository.save(usuario);
        }

        return "redirect:/dashboard/asignar-rol";
    }

    //Reporte
    @Autowired
    private ReporteService reporteService;

    @PostMapping("/reportar")
    public String reportarPublicacion(@RequestParam Integer postId, @RequestParam String motivo,
            @RequestParam String razon, Principal principal) {
        reporteService.guardarReporte(postId, motivo, razon, principal.getName());
        return "redirect:/"; // Cambia a la URL adecuada
    }

    @GetMapping("/dashboard/verreportes")
    public String verReportes(Model model) {
        List<Reporte> reportes = reporteService.obtenerTodosLosReportes();
        model.addAttribute("reportes", reportes);
        return "dashboard/ReportesDashboard";
    }

}
