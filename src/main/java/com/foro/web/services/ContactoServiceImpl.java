package com.foro.web.services;

import com.foro.web.*;
import com.foro.web.model.Contacto;
import com.foro.web.model.Usuario;
import com.foro.web.repository.ContactoRepository;
import com.foro.web.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactoServiceImpl implements ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ContactoEmailService emailService;
    @Override
    public List<Contacto> listaContacto() {
        return contactoRepository.findAll();
    }

    @Override
   public void saveContacto(Contacto contacto, Integer usuarioId, String correoUsuario) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + usuarioId));

    List<Contacto> contactos = contactoRepository.findByUsuarioIdOrderByTiempoEnvioDesc(usuarioId);

    if (!contactos.isEmpty()) {
        Contacto ultimoContacto = contactos.get(0);
        LocalDateTime fechaUltimoEnvio = ultimoContacto.getTiempoEnvio();
        LocalDateTime ahora = LocalDateTime.now();

        long horasDesdeUltimoEnvio = ChronoUnit.HOURS.between(fechaUltimoEnvio, ahora);
        if (horasDesdeUltimoEnvio < 24) {
        // Formatear la fecha y hora del último envío
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String fechaUltimoEnvioFormateada = fechaUltimoEnvio.format(formatter);
                
                // Incluir la fecha y hora en el mensaje
                throw new IllegalStateException("Debe esperar 24 horas desde el último envío para poder enviar otro reporte. "
                        + "Último envío realizado el " + fechaUltimoEnvioFormateada + ".");        }
    }

    contacto.setUsuario(usuario);
    contacto.setEstado("pendiente");
    contacto.setTiempoEnvio(LocalDateTime.now());
    contacto.setCorreoUsuario(correoUsuario); // Registra el correo del usuario
    contacto.setFechaLimiteVerificacion(LocalDateTime.now().plusMinutes(15)); // Tiempo límite de 15 minutos
    contactoRepository.save(contacto);

    emailService.enviarCorreoVerificacion(contacto);
}


    @Override
    public Page<Contacto> listaContactoPaginado(int page, int size) {
        return contactoRepository.findAll(PageRequest.of(page, size));
    }
    
    @Override
    public Page<Contacto> listaContactoPaginadoPorEstado(String estado, int page, int size) {
        return contactoRepository.findByEstado(estado, PageRequest.of(page, size));
    }
    
    @Override
    public void eliminarPorId(Long id) {
        contactoRepository.deleteById(id);
    }
    
    @Override
    public Page<Contacto> listaContactoPaginadoPorEstadoYMotivo(String estado, String motivo, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return contactoRepository.findByEstadoAndMotivo(estado, motivo, pageable);
    }

    
}