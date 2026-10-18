package com.JosepEzequiel.Modulo2Inserta.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profesor")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String apellidos;
    private String direccion;

    // Constructor vacío (obligatorio para JPA)
    public Profesor() {}

    // Constructor con parámetros
    public Profesor(String nombre, String apellidos, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
    }

    @OneToMany(mappedBy = "profesor")
    private List<Materia> materias;
    
    @Override
    public String toString() {
        return "Profesor{" + "id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", direccion=" + direccion + '}';
    }
    
    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}