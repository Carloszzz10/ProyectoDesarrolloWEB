/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.controller;

import com.foro.web.model.Comentario;
import com.foro.web.model.Comunidades;
import com.foro.web.model.Post;
import com.foro.web.model.Reporte;
import com.foro.web.model.Usuario;
import com.foro.web.repository.ComentarioRepository;
import com.foro.web.repository.ComunidadRepository;
import com.foro.web.repository.PostRepository;
import com.foro.web.repository.ReporteRepository;
import com.foro.web.repository.UsuarioRepository;
import com.foro.web.services.ComunidadService;
import com.foro.web.services.PostService;
import com.foro.web.services.ReporteService;
import com.foro.web.services.UsuarioServicio;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Carlos Abad
 */
@Controller
@RequestMapping("/comunidad")
public class ComunidadController {

    @Autowired
    private ComunidadService comunidadService;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private ComunidadRepository comunidadRepository;

    @Autowired
    private PostRepository postRepository;
    
      @Autowired
    private ComentarioRepository comentarioRepository;
    
      @Autowired
    private ReporteRepository reporteRepository;
      
      
       @Autowired
    private ReporteService reporteService;
    
   

   // Controlador para mostrar comunidades registradas
@GetMapping("/registrar")
public String mostrarFormularioRegistro(Model model) {
    List<Comunidades> comunidades = comunidadService.obtenerTodasLasComunidades(); // Obtener todas las comunidades
    model.addAttribute("comunidades", comunidades); // Pasar la lista de comunidades al modelo
    return "formulario_comunidad"; // Nombre del template
}


    
    
    // Lista de extensiones válidas para archivos de imagen
    private final List<String> imageExtensions = Arrays.asList("image/jpeg", "image/png", "image/gif");

    @PostMapping("/registrar")
    public String crearComunidad(@ModelAttribute Comunidades comunidad,
            @RequestParam("image") MultipartFile image, Model model) {
          
        
         // Validación de la imagen
        if (!image.isEmpty()) {
            String contentType = image.getContentType();
            // Verificar si el archivo subido es de un tipo de imagen válido
            if (!imageExtensions.contains(contentType)) {
                model.addAttribute("error", "El archivo subido no es una imagen válida. Por favor, suba un archivo JPG, PNG o GIF.");
                return "CrearComunidad";
            }
            
            try {
                
                String uploadsDir = "./uploads/";
                Path uploadPath = Paths.get(uploadsDir);
              
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

        
              
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Guardar el archivo en la ruta especificada
                Files.copy(image.getInputStream(), filePath);

                comunidad.setImagen("/uploads/" + fileName);
                
                
                  } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Hubo un problema al subir la imagen. Por favor, intente nuevamente.");
                return "CrearComunidad";
            }
        }
        comunidad.setFechaCreacion(LocalDateTime.now()); 

   
        comunidadService.crearComunidad(comunidad);


      
        return "redirect:/comunidad/comunidades"; 
    }
    
    
    @GetMapping("/comunidades")
    public String mostrarComunidades(Model model) {
        Usuario usuarioActual = usuarioServicio.obtenerUsuarioActual();
        List<Comunidades> comunidadesUnidas = usuarioActual != null ? usuarioActual.getComunidades() : List.of();

   
        model.addAttribute("comunidades", comunidadService.obtenerTodasLasComunidades());
        model.addAttribute("comunidadesUnidas", comunidadesUnidas);

        return "comunidades"; 
    }

  
    @PostMapping("/unirse")
    public String unirseAComunidad(@RequestParam("comunidadId") Integer comunidadId, Model model) {
      
        Usuario usuarioActual = usuarioServicio.obtenerUsuarioActual();

        if (usuarioActual != null) {
         
            comunidadService.unirseAComunidad(comunidadId);

         
            return "redirect:/comunidad/comunidades";
        } else {
            model.addAttribute("error", "Debes iniciar sesión para unirte a una comunidad.");

            model.addAttribute("comunidades", comunidadService.obtenerTodasLasComunidades());
            return "comunidades"; 
        }
    }
  

    @PostMapping("/salir")
    public String salirDeComunidad(@RequestParam("comunidadId") Integer comunidadId, Model model) {
   
        Usuario usuarioActual = usuarioServicio.obtenerUsuarioActual();

        if (usuarioActual != null) {
          
            comunidadService.salirDeComunidad(comunidadId);

        
            return "redirect:/comunidad/comunidades";
        } else {
            model.addAttribute("error", "Debes iniciar sesión para salir de una comunidad.");
      
            model.addAttribute("comunidades", comunidadService.obtenerTodasLasComunidades());
            return "comunidades"; 
        }
    }

    @GetMapping("/detalle/{id}")
    public String mostrarDetalleComunidad(@PathVariable("id") Integer comunidadId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el nombre del usuario autenticado

        Usuario usuario = usuarioRepository.findByUsuario(username); // Buscar usuario por su nombre en la BD

        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "redirect:/comunidad/comunidades";
        }

    
        Comunidades comunidad = comunidadService.obtenerComunidadPorId(comunidadId);

        if (comunidad == null) {
            model.addAttribute("error", "La comunidad no existe.");
            return "redirect:/comunidad/comunidades";
        }

   
        List<Comunidades> comunidadesUnidas = usuario.getComunidades();
        
        boolean usuarioUnido = comunidadesUnidas.contains(comunidad);

       
        List<Post> posts = postService.obtenerPostsPorComunidad(comunidadId);


        model.addAttribute("usuarioId", usuario.getId());
        model.addAttribute("nombres", usuario.getNombres());
        model.addAttribute("email", usuario.getUsuario());
        model.addAttribute("imagePerfil", usuario.getImagePerfil());
        model.addAttribute("comunidad", comunidad);
        model.addAttribute("usuarioUnido", usuarioUnido);
        model.addAttribute("posts", posts); // Agregar los posts al modelo

        if (usuarioUnido) {
            return "detalle_comunidad";
        } else {
            model.addAttribute("error", "Debes unirte a la comunidad para acceder a ella.");
            return "redirect:/comunidad/comunidades"; 
        }
    }

    @PostMapping("/{comunidadId}/post")
    public String publicarPostEnComunidad(@PathVariable("comunidadId") Integer comunidadId,
            @ModelAttribute Post post,
            @RequestParam("contenido") String contenido,
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("image") MultipartFile image) {
        // Validar existencia de la comunidad
        Comunidades comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new EntityNotFoundException("Comunidad no encontrada: " + comunidadId));

        // Validar existencia del usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + usuarioId));

        // Asignar valores al post
        post.setContenido(contenido);
        post.setUsuario(usuario);
        post.setComunidad(comunidad);
        post.setCreatedAt(LocalDateTime.now());

        // Procesar imagen
        if (!image.isEmpty()) {
            try {
                String uploadsDir = "./uploads/";
                Path uploadPath = Paths.get(uploadsDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = image.getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : "";

                String nombreUnico = UUID.randomUUID().toString() + extension;
                Path filePath = uploadPath.resolve(nombreUnico);
                Files.copy(image.getInputStream(), filePath);

                post.setImageUrl("/uploads/" + nombreUnico);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Guardar el post
        postService.savePostEnComunidad(post, usuarioId, comunidadId);

        // Redirigir a la vista de detalles de la comunidad
        return "redirect:/comunidad/detalle/" + comunidad.getId();
    }

@PostMapping("/{comunidadId}/post/editar")
public String editarPostEnComunidad(@PathVariable("comunidadId") Integer comunidadId,
                                    @RequestParam("postId") Integer postId,
                                    @RequestParam("contenido") String contenido,
                                    @RequestParam(required = false) MultipartFile image,
                                    Model model) {
    // Buscar el post
    Post post = postRepository.findById(postId)
            .orElse(null); // No lanzamos excepción, simplemente verificamos si es null

    if (post == null) {
        model.addAttribute("error", "Post no encontrado");
        return "error"; // Retornamos a una página de error si no se encuentra el post
    }

    // Actualizar contenido del post
    post.setContenido(contenido);

    // Manejo de imagen
    if (image != null && !image.isEmpty()) {
        String uploadsDir = "./uploads/";
        Path uploadPath = Paths.get(uploadsDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";

            String nombreUnico = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(nombreUnico);

           
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                Path oldImagePath = Paths.get(uploadsDir, Paths.get(post.getImageUrl()).getFileName().toString());
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                }
            }

          
            Files.copy(image.getInputStream(), filePath);
            post.setImageUrl("/uploads/" + nombreUnico);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar o eliminar la imagen.");
            return "error"; // Retornar error si ocurre un problema con la imagen
        }
    }

  
    postService.editarPostEnComunidad(post);

   
    return "redirect:/comunidad/detalle/" + comunidadId;
}


@GetMapping("/{comunidadId}/post/eliminar/{postId}")
public String eliminarPostEnComunidad(
    @PathVariable("comunidadId") Integer comunidadId,
    @PathVariable("postId") Integer postId, 
    Model model) {
    
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    Usuario usuario = usuarioRepository.findByUsuario(username);

 
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post no encontrado"));

   
    if (post.getUsuario().getId().equals(usuario.getId())) {
       
        List<Comentario> comentarios = comentarioRepository.findByPostId(postId);
        comentarioRepository.deleteAll(comentarios);

    
        List<Reporte> reportes = reporteRepository.findByPostId(postId);
        reporteRepository.deleteAll(reportes);


        postRepository.delete(post);
    }

    // Redirigir al detalle de la comunidad
    return "redirect:/comunidad/detalle/" + comunidadId;
}   



// Método para mostrar el formulario de edición de comunidad
@GetMapping("/editar/{id}")
public String mostrarFormularioEdicion(@PathVariable("id") Integer comunidadId, Model model) {
    Comunidades comunidad = comunidadRepository.findById(comunidadId)
            .orElseThrow(() -> new EntityNotFoundException("Comunidad no encontrada: " + comunidadId));
    model.addAttribute("comunidad", comunidad);
    return "editar_comunidad";
}

// Método para procesar la edición de comunidad
@PostMapping("/editar/{id}")
public String editarComunidad(@PathVariable("id") Integer comunidadId,
                               @ModelAttribute Comunidades comunidad,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               Model model) {
    Comunidades comunidadExistente = comunidadRepository.findById(comunidadId)
            .orElseThrow(() -> new EntityNotFoundException("Comunidad no encontrada: " + comunidadId));

    comunidadExistente.setNombre(comunidad.getNombre());
    comunidadExistente.setDescripcion(comunidad.getDescripcion());

    // Si se sube una nueva imagen, se valida y se guarda
    if (image != null && !image.isEmpty()) {
        String contentType = image.getContentType();
        if (!imageExtensions.contains(contentType)) {
            model.addAttribute("error", "El archivo subido no es una imagen válida.");
            return "editar_comunidad";
        }
        
        try {
            // Eliminar la imagen anterior si existe
            if (comunidadExistente.getImagen() != null && !comunidadExistente.getImagen().isEmpty()) {
                Path oldImagePath = Paths.get("./uploads", Paths.get(comunidadExistente.getImagen()).getFileName().toString());
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                }
            }

            // Guardar la nueva imagen
            String uploadsDir = "./uploads/";
            Path uploadPath = Paths.get(uploadsDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);

            comunidadExistente.setImagen("/uploads/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Hubo un problema al subir la imagen.");
            return "editar_comunidad";
        }
    }

    comunidadRepository.save(comunidadExistente);
    return "redirect:/comunidad/detalle/" + comunidadId;
}

// Método para eliminar una comunidad
@GetMapping("/eliminar/{id}")
public String eliminarComunidad(@PathVariable("id") Integer comunidadId, Model model) {
    Comunidades comunidad = comunidadRepository.findById(comunidadId)
            .orElseThrow(() -> new EntityNotFoundException("Comunidad no encontrada: " + comunidadId));

    // Eliminar todos los posts asociados a la comunidad
    List<Post> posts = postRepository.findByComunidadId(comunidadId);
    for (Post post : posts) {
        // Eliminar comentarios y reportes asociados a los posts
        List<Comentario> comentarios = comentarioRepository.findByPostId(post.getId());
        comentarioRepository.deleteAll(comentarios);
        List<Reporte> reportes = reporteRepository.findByPostId(post.getId());
        reporteRepository.deleteAll(reportes);
        postRepository.delete(post);
    }

    // Eliminar la comunidad
    comunidadRepository.delete(comunidad);

    return "redirect:/comunidad/comunidades"; // Redirige a la lista de comunidades después de eliminarla
}





}
