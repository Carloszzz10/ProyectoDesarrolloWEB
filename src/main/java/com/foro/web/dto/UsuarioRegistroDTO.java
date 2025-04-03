/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.dto;

import com.foro.web.model.Rol;
import com.foro.web.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author Carlos Abad
 */
public class UsuarioRegistroDTO {

    private Integer id;

    private String nombres;

    private String apellidos;

    private String usuario;

    private String imagePerfil;

    private Integer numero;

    private String SedeEstudio;

    private String sexo;

    private LocalDateTime FechaRegistro;

    private String password;
    
        private List<String> roles;

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UsuarioRegistroDTO(Integer id, String nombres, String apellidos, String usuario, String imagePerfil, Integer numero, String SedeEstudio, String sexo, LocalDateTime FechaRegistro, String password, List<String> roles) {
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
        this.roles = roles;
    }

    public UsuarioRegistroDTO(String nombres, String apellidos, String usuario, String imagePerfil, Integer numero, String SedeEstudio, String sexo, LocalDateTime FechaRegistro, String password, List<String> roles) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.imagePerfil = imagePerfil;
        this.numero = numero;
        this.SedeEstudio = SedeEstudio;
        this.sexo = sexo;
        this.FechaRegistro = FechaRegistro;
        this.password = password;
        this.roles = roles;
    }

    



  
    public UsuarioRegistroDTO(Usuario usuario) {
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.usuario = usuario.getUsuario();
        this.numero = usuario.getNumero();
        this.SedeEstudio = usuario.getSedeEstudio();
        this.sexo = usuario.getSexo();
        this.FechaRegistro = usuario.getFechaRegistro();
        this.roles = usuario.getRoles().stream()
                     .map(Rol::getNombres)
                     .collect(Collectors.toList());
    }
    public UsuarioRegistroDTO(String usuario) {
        this.usuario = usuario;
    }

    public UsuarioRegistroDTO() {

    }

}
