package com.foro.web.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "comunidad_id")
    private Comunidades comunidad; 
    
    public String getNombreComunidad() {
        return (comunidad != null && comunidad.getNombre() != null) ? comunidad.getNombre() : "Pública";
    }

    private String contenido;

    private String imageUrl;
    
@CreationTimestamp
    private LocalDateTime createdAt;

    public String getFormattedDate() {
    if (createdAt == null) {
        return "";
    }

    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(createdAt, now);

    // Formato para fechas completas (dd de MMMM a las HH:mm)
    DateTimeFormatter fullFormatter = DateTimeFormatter
            .ofPattern("d 'de' MMMM 'a las' HH:mm")
            .withLocale(new Locale("es", "ES"));

    if (duration.toHours() < 24) {
        if (duration.toMinutes() < 1) {
            return "hace unos segundos";
        } else if (duration.toMinutes() < 60) {
            return "hace " + duration.toMinutes() + " minutos";
        } else {
            return "hace " + duration.toHours() + " horas";
        }
    } else {
        // Si ha pasado más de un día, usar el formato completo
        return createdAt.format(fullFormatter);
    }
}

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Guardados> guardados;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Comunidades getComunidad() {
        return comunidad;
    }

    public void setComunidad(Comunidades comunidad) {
        this.comunidad = comunidad;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Guardados> getGuardados() {
        return guardados;
    }

    public void setGuardados(List<Guardados> guardados) {
        this.guardados = guardados;
    }

    public Post(Integer id, Usuario usuario, Comunidades comunidad, String contenido, String imageUrl, LocalDateTime createdAt, List<Guardados> guardados) {
        this.id = id;
        this.usuario = usuario;
        this.comunidad = comunidad;
        this.contenido = contenido;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.guardados = guardados;
    }

 
    
    public Post(){
        super();
    }
    
    
    
    

}
