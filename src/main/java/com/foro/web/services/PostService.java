/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.foro.web.services;

import com.foro.web.model.Comunidades;
import com.foro.web.model.Post;
import com.foro.web.model.Usuario;
import com.foro.web.repository.ComunidadRepository;
import com.foro.web.repository.PostRepository;
import com.foro.web.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author geuse
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComunidadRepository comunidadRepository;

    @Transactional
    public Post savePost(Post post, Integer usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + usuarioId));

        post.setUsuario(usuario);

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Post> findByUser(Integer usuarioId, Pageable pageable) {
        return postRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId, pageable);
    }

    public Post findById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        postRepository.deleteById(id);
    }

    public Post obtenerPostPorId(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con ID: " + postId));
    }

    public long countByUsuarioId(Integer usuarioId) {
        return postRepository.countByUsuarioId(usuarioId);
    }

    @Transactional
    public Post savePostEnComunidad(Post post, Integer usuarioId, Integer comunidadId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + usuarioId));

        Comunidades comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new EntityNotFoundException("Comunidad no encontrada: " + comunidadId));

        post.setUsuario(usuario);
        post.setComunidad(comunidad);
        post.setCreatedAt(LocalDateTime.now()); // Establecer la fecha de creación

        // Guardar el post
        return postRepository.save(post);
    }

    // Método para obtener publicaciones de las comunidades a las que el usuario está unido
    public List<Post> obtenerPostsPorUsuario(Usuario usuario) {
        // Obtén las comunidades a las que el usuario está unido
        List<Comunidades> comunidades = usuario.getComunidades();

        // Filtra las publicaciones para incluir solo las de las comunidades a las que el usuario está unido
        return postRepository.findAll().stream()
                .filter(post -> comunidades.contains(post.getComunidad()))
                .collect(Collectors.toList());
    }

    public List<Post> obtenerPostsPorComunidad(Integer comunidadId) {
        return postRepository.findByComunidadIdOrderByCreatedAtDesc(comunidadId);
    }

    @Transactional
    public void editarPostEnComunidad(Post post) {
        // Verificar que el post existe
        postRepository.findById(post.getId())
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado: " + post.getId()));

        // Guardar los cambios
        postRepository.save(post);
    }

    public List<Post> obtenerPostsPorComunidades(List<Comunidades> comunidades) {
        // Filtrar y devolver los posts que pertenecen a las comunidades
        return postRepository.findByComunidadIn(comunidades);
    }

    public List<Post> obtenerPostsSinComunidad() {
        // Filtrar y devolver los posts que no están relacionados con ninguna comunidad
        return postRepository.findByComunidadIsNull();
    }
}
