package com.JosepEzequiel.Modulo2Inserta.dao;

import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;

import java.util.List;
import java.util.Optional;

public interface MateriaDAO {

    Optional<Materia> save(Materia materia);
    Optional<Materia> update(Materia materia);
    boolean delete(int id);
    List<Materia> findAll();
    Optional<Materia> findById(int id);

}
