package com.JosepEzequiel.Modulo2Inserta.dao;

import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;

import java.util.List;
import java.util.Optional;

public interface MateriaDAO {

    Optional<Materia> createMateria(Materia materia);
    Optional<Materia> updateMateria(Materia materia);
    boolean deleteMateria(int id);
    List<Materia> findAllMaterias();
    Optional<Materia> findMateriaById(int id);
    Optional<Materia> findMateriaByNombre(String nombre);
    Optional<Profesor> findProfesorById(int id);
    //Materia findMateriaByProfesor(Profesor profesor); a implementar luego
}
