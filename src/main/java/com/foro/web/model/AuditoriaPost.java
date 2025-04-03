package com.foro.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_post")
public class AuditoriaPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioQueRealizaAccion;

    @Column(name = "accion")
    private String accion; // "ELIMINADO" o "EDITADO"

    @Column(name = "contenido_original", columnDefinition = "TEXT")
    private String contenidoOriginal;

    @Column(name = "imagen_original")
    private String imagenOriginal;

    @Column(name = "fecha_accion")
    private LocalDateTime fechaAccion;

    // Constructores
    public AuditoriaPost() {}

    public AuditoriaPost(Integer postId, Usuario usuarioQueRealizaAccion, String accion, 
                         String contenidoOriginal, String imagenOriginal) {
        this.postId = postId;
        this.usuarioQueRealizaAccion = usuarioQueRealizaAccion;
        this.accion = accion;
        this.contenidoOriginal = contenidoOriginal;
        this.imagenOriginal = imagenOriginal;
        this.fechaAccion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Usuario getUsuarioQueRealizaAccion() {
        return usuarioQueRealizaAccion;
    }

    public void setUsuarioQueRealizaAccion(Usuario usuarioQueRealizaAccion) {
        this.usuarioQueRealizaAccion = usuarioQueRealizaAccion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getContenidoOriginal() {
        return contenidoOriginal;
    }

    public void setContenidoOriginal(String contenidoOriginal) {
        this.contenidoOriginal = contenidoOriginal;
    }

    public String getImagenOriginal() {
        return imagenOriginal;
    }

    public void setImagenOriginal(String imagenOriginal) {
        this.imagenOriginal = imagenOriginal;
    }

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    
}
