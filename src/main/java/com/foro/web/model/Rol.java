package com.foro.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombres;

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

    public Rol(Integer id, String nombres) {
        this.id = id;
        this.nombres = nombres;
    }

    public Rol() {
        super();
    }

    public Rol(String nombres) {
        this.nombres = nombres;
    }

   @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rol rol = (Rol) o;
    return Objects.equals(id, rol.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
    

}
