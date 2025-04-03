package com.foro.web.services;

import com.foro.web.model.Baneo;
import com.foro.web.model.Usuario;
import com.foro.web.repository.BaneoRepository;
import com.foro.web.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BaneoService {
    @Autowired
    private BaneoRepository baneoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void banearUsuario(String username, LocalDateTime fechaFin, String razon, Integer administradorId) {
        Usuario usuario = usuarioRepository.findByUsuario(username);
        if (usuario != null) {
            // Desactivar baneos previos
            Optional<Baneo> baneoExistente = baneoRepository.findByUsuarioAndActivoTrue(usuario);
            baneoExistente.ifPresent(baneo -> {
                baneo.setActivo(false);
                baneoRepository.save(baneo);
            });

            // Crear nuevo baneo
            Baneo nuevoBaneo = new Baneo(usuario, LocalDateTime.now(), fechaFin, razon, administradorId);
            baneoRepository.save(nuevoBaneo);
        }
    }

    public boolean usuarioEstaBaneado(String username) {
        Usuario usuario = usuarioRepository.findByUsuario(username);
        if (usuario == null) return false;

        return baneoRepository.findByUsuarioAndFechaFinAfter(usuario, LocalDateTime.now())
                .stream()
                .anyMatch(baneo -> baneo.getActivo());
    }

    public void desbanearUsuario(String username) {
    Usuario usuario = usuarioRepository.findByUsuario(username);
    if (usuario != null) {
        List<Baneo> baneos = baneoRepository.findByUsuario(usuario);
        baneos.forEach(baneo -> {
            if (baneo.getActivo()) {
                baneo.setActivo(false);
                baneo.setFechaFin(LocalDateTime.now());
            }
        });
        baneoRepository.saveAll(baneos);
    }
}
}
