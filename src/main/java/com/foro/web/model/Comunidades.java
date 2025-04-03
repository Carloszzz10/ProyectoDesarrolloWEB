package com.foro.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comunidades")
public class Comunidades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL)
    private List<Post> posts; 

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToMany
    @JoinTable(
        name = "usuario_comunidad",
        joinColumns = @JoinColumn(name = "comunidad_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios; 

    @Column(name = "imagen")  
    private String imagen; 

     @Transient
  private boolean usuarioUnido = false; // Indica si el usuario está en la comunidad o no

    // Este método establece si el usuario está unido a la comunidad
   public void verificarUsuarioUnido(String username) {
        this.usuarioUnido = this.usuarios != null && this.usuarios.stream()
            .anyMatch(usuario -> usuario.getUsuario().equals(username));
    }
   
      public Comunidades() {
        this.usuarios = new ArrayList<>();
        this.usuarioUnido = false;
    }
   
 
  

    // Métodos getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

  

    public Comunidades(Integer id, String nombre, String descripcion, List<Post> posts, LocalDateTime fechaCreacion, List<Usuario> usuarios, String imagen, boolean usuarioUnido) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posts = posts;
        this.fechaCreacion = fechaCreacion;
        this.usuarios = usuarios;
        this.imagen = imagen;
        this.usuarioUnido = usuarioUnido;
    }
    
    
}
