package com.JosepEzequiel.Modulo2Inserta.model;

import jakarta.persistence.*;

@Entity
@Table(name="materias")
public class Materia {

    //Constructores


    public Materia() {
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

    @Column(name="nombre")
    private String nombre;

    @Column(name="num_creditos")
    private int numCreditos;

    @Column(name="horas_credito")
    private int horasCredito;

    /*@ManyToOne
    @JoinColumn("id_profesor")
    private int idProfesor; -> A desarrollar luego */

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

//Desde que id se autogenera, este no tiene setter para evitar conflictos
    //por id coincidentes

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumCreditos(int numCreditos) {
        this.numCreditos = numCreditos;
    }

    public void setHorasCredito(int horasCredito) {
        this.horasCredito = horasCredito;
    }

    //ToString
}
