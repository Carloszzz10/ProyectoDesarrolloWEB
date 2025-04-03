 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "Usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "usuario"))

public class Usuario  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "usuario")
    private String usuario;

    private String imagePerfil;

    private Integer numero;

    private String SedeEstudio;

    private String sexo;

    private LocalDateTime  FechaRegistro;

    private String password;
    
    
    @Column(name = "activo")
    private Boolean activo = false;
    
 @Column(name = "tokenActivacion")
    private String tokenActivacion;
 
 
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id")
    )
    private Set<Rol> roles = new HashSet<>();
    
    
         @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Post> posts;
    
    
     @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();
     
     
      @ManyToMany(mappedBy = "usuarios")
       private List<Comunidades> comunidades;
      
      
      

     public Usuario(String nombres, String apellidos, String usuario, String imagePerfil, Integer numero, String SedeEstudio, String sexo, LocalDateTime FechaRegistro, String password, Set<Rol> roles) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.imagePerfil = imagePerfil;
        this.numero = numero;
        this.SedeEstudio = SedeEstudio;
        this.sexo = sexo;
        this.FechaRegistro = FechaRegistro;
        this.password = password;
        this.roles = (Set<Rol>) roles;
    }

    public Usuario(Integer id, String nombres, String apellidos, String usuario, String imagePerfil, Integer numero, String SedeEstudio, String sexo, LocalDateTime FechaRegistro, String password, String tokenActivacion, List<Post> posts, List<Comunidades> comunidades) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.imagePerfil = imagePerfil;
        this.numero = numero;
        this.SedeEstudio = SedeEstudio;
        this.sexo = sexo;
        this.FechaRegistro = FechaRegistro;
        this.password = password;
        this.tokenActivacion = tokenActivacion;
        this.posts = posts;
        this.comunidades = comunidades;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImagePerfil() {
        return imagePerfil;
    }

    public void setImagePerfil(String imagePerfil) {
        this.imagePerfil = imagePerfil;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getSedeEstudio() {
        return SedeEstudio;
    }

    public void setSedeEstudio(String SedeEstudio) {
        this.SedeEstudio = SedeEstudio;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDateTime getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime FechaRegistro) {
        this.FechaRegistro = FechaRegistro;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getTokenActivacion() {
        return tokenActivacion;
    }

    public void setTokenActivacion(String tokenActivacion) {
        this.tokenActivacion = tokenActivacion;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Comunidades> getComunidades() {
        return comunidades;
    }

    public void setComunidades(List<Comunidades> comunidades) {
        this.comunidades = comunidades;
    }

    public Usuario(String nombres, String apellidos, String usuario, String imagePerfil, Integer numero, String SedeEstudio, String sexo, LocalDateTime FechaRegistro, String password, String tokenActivacion, List<Post> posts, List<Comunidades> comunidades) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.imagePerfil = imagePerfil;
        this.numero = numero;
        this.SedeEstudio = SedeEstudio;
        this.sexo = sexo;
        this.FechaRegistro = FechaRegistro;
        this.password = password;
        this.tokenActivacion = tokenActivacion;
        this.posts = posts;
        this.comunidades = comunidades;
    }

      
      
     




    public Usuario() {
        this.roles = new HashSet<>(); 
    }

  
 
}
