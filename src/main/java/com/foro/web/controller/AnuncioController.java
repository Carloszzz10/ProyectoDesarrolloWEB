package com.foro.web.controller;

import com.foro.web.model.Anuncio;
import com.foro.web.model.Contacto;
import com.foro.web.model.Usuario;
import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.ContactoService;
import com.foro.web.services.AnuncioService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar los anuncios para todos los usuarios
    @GetMapping("/Anuncios")
    public String mostrarAnuncios(Model model, @RequestParam(defaultValue = "0") int page) {
        // Obtener el anuncio más reciente
        Anuncio anuncioReciente = anuncioService.getAnuncioMasReciente();
        model.addAttribute("anuncioReciente", anuncioReciente);

        // Obtener anuncios paginados
        Page<Anuncio> anuncios = anuncioService.getAllAnuncios(PageRequest.of(page, 20, Sort.by("createdAt").descending()));
        model.addAttribute("anuncios", anuncios);
        return "anuncios"; // Vista donde se mostrarán los anuncios
    }

    // Mostrar el dashboard de anuncios para el administrador
    @GetMapping(value = {"/dashboard/Anuncios"})
    public String mostrarAnuncio(Model model, @RequestParam(defaultValue = "0") int page) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsuario(username);

        if (usuario != null) {

            model.addAttribute("usuarioId", usuario.getId());
            model.addAttribute("imagePerfil", usuario.getImagePerfil());
        }
  
        Page<Anuncio> anuncios = anuncioService.getAllAnuncios(PageRequest.of(page, 20));
        List<Contacto> contactos = contactoService.listaContacto();

        model.addAttribute("usuarios", usuarioRepository.findAll());

        model.addAttribute("contactos", contactos);
        model.addAttribute("anuncios", anuncios);
        return "dashboard/AnunciosDashboard";
    }

    // Publicar un nuevo anuncio
    @PostMapping("/dashboard/Anuncios")
    public String publicarAnuncio(
            @ModelAttribute Anuncio anuncio,
            @RequestParam("titulo") String titulo,
            @RequestParam("contenido") String contenido,
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("image") MultipartFile image) {

        if (!image.isEmpty()) {
            try {
                String uploadsDir = "./uploads/";
                Path uploadPath = Paths.get(uploadsDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Obtener el nombre del archivo original
                String originalFilename = image.getOriginalFilename();

                // Reemplazar espacios por guiones bajos o guiones
                String nombreLimpio = originalFilename.replaceAll("\\s+", "_");

                // Obtener la extensión del archivo
                String extension = nombreLimpio.substring(nombreLimpio.lastIndexOf("."));

                // Crear un nombre único (puedes combinar el nombre limpio con un timestamp)
                String nombreUnico = System.currentTimeMillis() + "_" + nombreLimpio;

                // Guardar la imagen en la ruta especificada
                Path filePath = uploadPath.resolve(nombreUnico);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Guardar la URL de la imagen en el anuncio
                anuncio.setImageUrl("/uploads/" + nombreUnico);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Configuración del anuncio
        anuncio.setTitulo(titulo);
        anuncio.setContenido(contenido);
        anuncio.setCreatedAt(LocalDateTime.now());
        anuncioService.saveAnuncio(anuncio, usuarioId);

        return "redirect:/dashboard/Anuncios";
    }

    // Eliminar un anuncio
    @GetMapping("/dashboard/Anuncios/eliminar/{anuncioId}")
    public String eliminarAnuncio(@PathVariable Long anuncioId, Model model) {

        Anuncio anuncio = anuncioService.findById(anuncioId);

        if (anuncio != null) {
            anuncioService.delete(anuncioId);
        }

        return "redirect:/dashboard/Anuncios";
    }

}
