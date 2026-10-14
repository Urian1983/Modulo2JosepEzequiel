package com.JosepEzequiel.Modulo2Inserta.repository;

import com.JosepEzequiel.Modulo2Inserta.model.Materia;

import java.util.List;
import java.util.Optional;

public interface MateriaDAO {

    void createMateria(Materia materia);
    void updateMateria(Materia materia, int id);
    void deleteMateria(int id);
    List<Materia> findAllMaterias();
    Optional<Materia> findMateriaById(int id);
    Optional<Materia> findMateriaByNombre(String nombre);
    //Materia findMateriaByProfesor(Profesor profesor); a implementar luego
}
