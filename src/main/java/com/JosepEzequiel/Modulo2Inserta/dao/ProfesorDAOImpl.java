package com.JosepEzequiel.Modulo2Inserta.dao;

import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ProfesorDAOImpl implements ProfesorDAO {

     @Autowired
     private EntityManager entityManager;
     
     @Override
     @Transactional
     public void save(Profesor theProfesor) {
         entityManager.persist(theProfesor);
     }
    
     @Override
     public Profesor findById(Integer id) {

         return entityManager.find(Profesor.class, id);
     }

     @Override
     @Transactional
     public void update(Profesor theProfesor) {
         entityManager.merge(theProfesor);
     }

     @Override
     public List<Profesor> findAll() {
         TypedQuery<Profesor> query = entityManager.createQuery
                 ("FROM Profesor", Profesor.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        Profesor profesor = this.findById(id);
        if(!(profesor == null)) {
            entityManager.remove(profesor);
            return true;
        }
        return false;
    }

    @Override
    public List<Materia> findMaterias(int profesorId) {
        return entityManager.createQuery(
                "SELECT m FROM Materia WHERE m.profesor.id = :id", Materia.class)
                .setParameter("id",profesorId)
                .getResultList();
    }
}