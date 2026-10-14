package com.JosepEzequiel.Modulo2Inserta.repository;

import com.JosepEzequiel.Modulo2Inserta.model.Materia;

import java.util.List;
import java.util.Optional;

public interface MateriaDAO {

    Optional<Materia> createMateria(Materia materia);
    Optional<Materia> updateMateria(Materia materia);
    boolean deleteMateria(int id);
    List<Materia> findAllMaterias();
    Optional<Materia> findMateriaById(int id);
    Optional<Materia> findMateriaByNombre(String nombre);
    //Materia findMateriaByProfesor(Profesor profesor); a implementar luego
}
