package com.foro.web.services;

import com.foro.web.model.Post;
import com.foro.web.model.Reporte;
import com.foro.web.model.Usuario;
import com.foro.web.repository.PostRepository;
import com.foro.web.repository.ReporteRepository;
import com.foro.web.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostRepository postRepository;

    public void guardarReporte(Integer postId, String motivo, String razon, String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByUsuario(nombreUsuario);
        Post post = postRepository.findById(postId).orElseThrow();

        Reporte reporte = new Reporte();
        reporte.setPost(post);
        reporte.setUsuario(usuario);
        reporte.setMotivo(motivo);
        reporte.setRazon(razon);

        reporteRepository.save(reporte);
    }
    
    public List<Reporte> obtenerTodosLosReportes() {
        return reporteRepository.findAll();
    }
}

