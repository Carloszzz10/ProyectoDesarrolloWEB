package com.foro.web.controller;

import com.foro.web.model.Anuncio;
import com.foro.web.model.AuditoriaPost;
import com.foro.web.model.Comentario;
import com.foro.web.model.Comunidades;

import com.foro.web.model.Post;
import com.foro.web.model.Reporte;
import com.foro.web.model.Usuario;
import com.foro.web.repository.AuditoriaPostRepository;
import com.foro.web.repository.ComentarioRepository;
import com.foro.web.repository.ReporteRepository;
import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.AnuncioService;
import com.foro.web.services.ContactoService;
import com.foro.web.services.PostService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.UUID;
import org.springframework.data.domain.Sort;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private ContactoService contactoService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AnuncioService anuncioService;
    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private AuditoriaPostRepository auditoriaPostRepository;

    ;

    @GetMapping(value = {"/", "", "index"})
    public String mostrarPost(Model model, @RequestParam(defaultValue = "0") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsuario(username);

        if (usuario != null) {
            model.addAttribute("usuarioId", usuario.getId());
            model.addAttribute("imagePerfil", usuario.getImagePerfil());
            model.addAttribute("nombres", usuario.getNombres());
            model.addAttribute("email", usuario.getUsuario());

            List comunidadesUnidas = usuario.getComunidades() != null ? usuario.getComunidades() : new ArrayList<>();
            model.addAttribute("comunidadesUnidas", comunidadesUnidas);

            List<Post> postsComunidad = postService.obtenerPostsPorComunidades(comunidadesUnidas);
            model.addAttribute("postsComunidad", postsComunidad);
        }

        List<Post> postsSinComunidad = postService.obtenerPostsSinComunidad();
        model.addAttribute("postsSinComunidad", postsSinComunidad);

        List<Post> todosLosPosts = new ArrayList<>();

        if (usuario != null) {
            todosLosPosts.addAll(postService.obtenerPostsPorComunidades(usuario.getComunidades()));
        }
        todosLosPosts.addAll(postsSinComunidad);
        todosLosPosts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
        model.addAttribute("posts", todosLosPosts);

        Anuncio anuncioReciente = anuncioService.getAnuncioMasReciente();
        if (anuncioReciente != null) {

            String fechaFormateada = anuncioReciente.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            model.addAttribute("anuncioReciente", anuncioReciente);
            model.addAttribute("fechaFormateada", fechaFormateada);

            boolean mostrarVerMas = anuncioReciente.getContenido() != null
                    && anuncioReciente.getContenido().length() > 100;
            model.addAttribute("mostrarVerMas", mostrarVerMas);
        } else {
            model.addAttribute("anuncioReciente", null);
            model.addAttribute("mostrarVerMas", false);
        }

        return "index";
    }

    @PostMapping("/post")
    public String publicarPost(@ModelAttribute Post post, @RequestParam("contenido") String contenido,
            @RequestParam("usuarioId") Integer usuarioId, @RequestParam("image") MultipartFile image) {

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

                // Genera un nombre único usando UUID
                String nombreUnico = UUID.randomUUID().toString() + extension;

                Path filePath = uploadPath.resolve(nombreUnico);
                Files.copy(image.getInputStream(), filePath);

                post.setImageUrl("/uploads/" + nombreUnico);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        post.setCreatedAt(LocalDateTime.now());
        postService.savePost(post, usuarioId);
        return "redirect:/";
    }

    @GetMapping("/MisPublicaciones")
    public String misPosts(Model model, @RequestParam(defaultValue = "0") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsuario(username);

        if (usuario != null) {
            model.addAttribute("usuarioId", usuario.getId());
            model.addAttribute("imagePerfil", usuario.getImagePerfil());
        }

        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postService.findByUser(usuario.getId(), pageRequest);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("posts", posts);

        return "misPublicaciones";
    }

    @GetMapping("/post/eliminar/{postId}")
public String eliminarPost(@PathVariable Integer postId, Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Usuario usuarioActual = usuarioRepository.findByUsuario(username);
    
    Post post = postService.findById(postId);
    if (post != null && post.getUsuario().getId().equals(usuarioActual.getId())) {
        // Crear registro de auditoría antes de eliminar
        AuditoriaPost auditoria = new AuditoriaPost(
            post.getId(), 
            usuarioActual,  // Pasamos el usuario completo
            "ELIMINADO", 
            post.getContenido(), 
            post.getImageUrl()
        );
        auditoriaPostRepository.save(auditoria);
        
        // Eliminar comentarios y reportes relacionados
        List comentarios = comentarioRepository.findByPostId(postId);
        comentarioRepository.deleteAll(comentarios);
        List reportes = reporteRepository.findByPostId(postId);
        reporteRepository.deleteAll(reportes);
        
        // Eliminar el post
        postService.delete(postId);
    }
    return "redirect:/";
}

@PostMapping("/post/editar")
public String editarPost(@RequestParam Integer postId, @RequestParam String contenido,
        @RequestParam(required = false) MultipartFile image, Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Usuario usuarioActual = usuarioRepository.findByUsuario(username);
    Post post = postService.findById(postId);
    if (post != null) {
        // Crear auditoría para la edición
        AuditoriaPost auditoria = new AuditoriaPost(
            post.getId(),
            usuarioActual,
            "EDITADO",
            post.getContenido(),
            post.getImageUrl()
        );

        // Guardar los valores originales antes de modificar
        String contenidoOriginal = post.getContenido();
        String imagenOriginal = post.getImageUrl();

        // Actualizar el contenido del post
        post.setContenido(contenido);
        LocalDateTime fechaCreacionOriginal = post.getCreatedAt();

        // Procesar nueva imagen si se subió
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
                String Nombreunico = UUID.randomUUID().toString() + extension;
                Path filePath = uploadPath.resolve(Nombreunico);
                Files.copy(image.getInputStream(), filePath);
                post.setImageUrl("/uploads/" + Nombreunico);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Restaurar la fecha original
        post.setCreatedAt(fechaCreacionOriginal);

        // Guardar los cambios en el post
        postService.savePost(post, post.getUsuario().getId());

        // Guardar auditoría
        auditoriaPostRepository.save(auditoria);
    }
    return "redirect:/";
}


    @GetMapping("dashboard/auditoria")
    public String mostrarAuditoriaPosts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Primero obtén el usuario
        Usuario usuario = usuarioRepository.findByUsuario(username);

        // Luego busca las auditorías de ese usuario
        List<AuditoriaPost> auditorias = auditoriaPostRepository.findByUsuarioQueRealizaAccion(usuario);
        model.addAttribute("auditorias", auditorias);
        return "dashboard/auditoria";
    }

}
