package com.JosepEzequiel.Modulo2Inserta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="materias")
public class Materia {

    //Constructores
    public Materia() {
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Materia(String nombre) {
        this.nombre = nombre;
    }

    public Materia(int numCreditos, String nombre, int horasCredito) {
        this.numCreditos = numCreditos;
        this.nombre = nombre;
        this.horasCredito = horasCredito;
    }

    //Variables

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nombre", nullable=false)
    private String nombre;

    @Column(name="num_creditos")
    private int numCreditos;

    @Column(name="horas_credito")
    private int horasCredito;

    @ManyToOne
    @JsonIgnoreProperties("materias")
    @JoinColumn(name="id_profesor")
    private Profesor profesor;

    //Getters y Setters

    public int getId() {
        return id;
    }

    public int getNumCreditos() {
        return numCreditos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHorasCredito() {
        return horasCredito;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumCreditos(int numCreditos) {
        this.numCreditos = numCreditos;
    }

    public void setHorasCredito(int horasCredito) {
        this.horasCredito = horasCredito;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    //ToString

    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numCreditos=" + numCreditos +
                ", horasCredito=" + horasCredito +
                ", profesor=" + profesor +
                '}';
    }
}
