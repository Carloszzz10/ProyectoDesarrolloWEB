package com.foro.web.services;

import com.foro.web.model.Anuncio;
import com.foro.web.model.Usuario;
import com.foro.web.repository.AnuncioRepository;
import com.foro.web.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnuncioService {
    @Autowired
    private AnuncioRepository anuncioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Guardar un nuevo anuncio
    @Transactional
    public Anuncio saveAnuncio(Anuncio anuncio, Integer usuarioId) {
        // Buscar usuario por ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + usuarioId));
        
        // Asignar usuario al anuncio
        anuncio.setUsuario(usuario);
        
        // Guardar el anuncio
        return anuncioRepository.save(anuncio);
    }
    
    // Obtener todos los anuncios, paginados
    public Page<Anuncio> getAllAnuncios(Pageable pageable) {
        return anuncioRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    // Obtener anuncios de un usuario específico, paginados
    public Page<Anuncio> findByUser(Integer usuarioId, Pageable pageable) {
        return anuncioRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId, pageable);
    }
    
    // Obtener un anuncio por ID
    public Anuncio findById(Long id) {
        return anuncioRepository.findById(id).orElse(null);
    }
    
    // Eliminar un anuncio por ID
    public void delete(Long id) {
        anuncioRepository.deleteById(id);
    }

    // Obtener el anuncio más reciente
    public Anuncio getAnuncioMasReciente() {
        return anuncioRepository.findTopByOrderByCreatedAtDesc();
    }
}