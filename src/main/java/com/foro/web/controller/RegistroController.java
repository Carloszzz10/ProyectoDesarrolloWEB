package com.foro.web.controller;

import com.foro.web.dto.UsuarioRegistroDTO;
import com.foro.web.model.Comunidades;
import com.foro.web.model.Post;
import com.foro.web.model.Usuario;
import com.foro.web.repository.ComunidadRepository;

import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.ComentarioService;
import com.foro.web.services.PostService;
import com.foro.web.services.UsuarioServicio;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioServicio usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ComunidadRepository comunidadRepository;

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }

    @GetMapping("Registro")
    public String mostrarFormRegistro() {
        return "registro";
    }

    @PostMapping("/Registroexitoso")
    public String RegistrarCuentaUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO usuarioRegistroDTO,
            @RequestParam("image") MultipartFile image, Model model) {
        if (usuarioService.existeEmail(usuarioRegistroDTO.getUsuario())) {
            model.addAttribute("errorMessage", "Este correo ya está registrado.");
            return "registro";
        }

        if (!image.isEmpty()) {
            try {
                String uploadsDir = "./uploads/";
                Path uploadPath = Paths.get(uploadsDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = image.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String nombreUnico = UUID.randomUUID().toString() + extension;

                Path filePath = uploadPath.resolve(nombreUnico);
                Files.copy(image.getInputStream(), filePath);

                usuarioRegistroDTO.setImagePerfil("/uploads/" + nombreUnico);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error al subir la imagen.");
                return "registro";
            }
        }

        usuarioService.GuardarUsuario(usuarioRegistroDTO);
        return "Registroexitoso";
    }

    @GetMapping("/confirmar-cuenta")
    public String confirmarCuenta(@RequestParam("token") String token, Model model) {
        System.out.println("Token recibido: " + token); // <-- Agrega esto para ver si llega el token
        boolean isConfirmed = usuarioService.confirmarCuenta(token);

        if (isConfirmed) {
            model.addAttribute("successMessage", "Cuenta activada exitosamente. Ahora puedes iniciar sesión.");
            return "login";  // Redirige a la página de inicio de sesión
        } else {
            model.addAttribute("errorMessage", "Token de activación inválido o expirado.");
            return "error";
        }
    }

    @GetMapping("/PerfilUsuario")
    public String MostrarPerfilUsuario(@AuthenticationPrincipal UserDetails userDetails, Model model, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Usuario usuario = usuarioRepository.findByUsuario(userDetails.getUsername());
        UsuarioRegistroDTO perfilDTO = new UsuarioRegistroDTO(usuario);

        long totalPublicaciones = postService.countByUsuarioId(usuario.getId());

        long totalComentarios = comentarioService.countByUsuarioId(usuario.getId());

        Page<Post> ultimosPosts = postService.findByUser(usuario.getId(), pageable);

        long totalcomunidades = comunidadRepository.countByUsuariosId(usuario.getId());

        
            List<String> nombresComunidades = new ArrayList<>();
for (Comunidades comunidad : usuario.getComunidades()) {
    nombresComunidades.add(comunidad.getNombre());
}

        
        
        model.addAttribute("perfil", perfilDTO);
        model.addAttribute("imagenPerfil", usuario.getImagePerfil());
        model.addAttribute("imagePerfil", usuario.getImagePerfil());
        model.addAttribute("totalComentarios", totalComentarios);
        model.addAttribute("totalcomunidades", totalcomunidades);
        model.addAttribute("totalPublicaciones", totalPublicaciones);
  model.addAttribute("nombresComunidades", nombresComunidades);
        model.addAttribute("ultimosPosts", ultimosPosts);

        return "miperfil";
    }

}
