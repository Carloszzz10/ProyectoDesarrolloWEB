package com.foro.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacto")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String motivo;
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "tiempo_envio", nullable = false, updatable = false)
    private LocalDateTime tiempoEnvio;

    @PrePersist
    protected void onCreate() {
        tiempoEnvio = LocalDateTime.now();
    }
    
    @Column(name = "fecha_limite_verificacion")
    private LocalDateTime fechaLimiteVerificacion;
    
    private String correoUsuario;
    
    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getTiempoEnvio() {
        return tiempoEnvio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTiempoEnvio(LocalDateTime tiempoEnvio) {
        this.tiempoEnvio = tiempoEnvio;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public LocalDateTime getFechaLimiteVerificacion() {
        return fechaLimiteVerificacion;
    }

    public void setFechaLimiteVerificacion(LocalDateTime fechaLimiteVerificacion) {
        this.fechaLimiteVerificacion = fechaLimiteVerificacion;
    }
    
    
    
}
