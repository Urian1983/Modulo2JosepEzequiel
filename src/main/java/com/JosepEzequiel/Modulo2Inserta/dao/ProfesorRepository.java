package com.JosepEzequiel.Modulo2Inserta.dao;

import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    
 }