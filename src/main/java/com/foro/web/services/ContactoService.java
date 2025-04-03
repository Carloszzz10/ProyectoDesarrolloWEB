package com.foro.web.services;

import com.foro.web.*;
import com.foro.web.model.Contacto;
import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public interface ContactoService {
 
    List<Contacto> listaContacto();
    void saveContacto(Contacto contacto, Integer contactoId, String contactoUsuario);
    Page<Contacto> listaContactoPaginado(int page, int size);
    Page<Contacto> listaContactoPaginadoPorEstado(String estado, int page, int size);
    void eliminarPorId(Long id);
    public Page<Contacto> listaContactoPaginadoPorEstadoYMotivo(String estado, String motivo, int page, int pageSize);
}