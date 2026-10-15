/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.JosepEzequiel.Modulo2Inserta.dao;

import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import java.util.List;


public interface ProfesorDAO {
    
    void save(Profesor theProfesor);
    Profesor findById(Integer id);
       
     List<Profesor> findByLastName(String lastName);
           
    void update(Profesor theProfesor);
    
    List<Materia> findMaterias();

    List<Profesor> findAll();
    
    boolean removeProfesor(int id);
    
}


