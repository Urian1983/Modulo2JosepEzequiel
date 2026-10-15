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
     public List<Materia> findMaterias(){
       TypedQuery<Materia> query = entityManager.createQuery("FROM Materia WHERE id = :id", Materia.class);
               return query.getResultList();
     }
     @Override
     public List<Profesor> findByLastName(String lastNameString) {
         TypedQuery<Profesor> theQuery = entityManager.createQuery( 
                 "FROM Profesor WHERE apellidos = :theLastName", Profesor.class);
         return theQuery.getResultList();
     }
      
     @Override
     @Transactional
     public void update(Profesor theProfesor) {
         entityManager.merge(theProfesor);
     }

    @Override
    public List<Profesor> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    public boolean removeProfesor(int id) {
        Profesor Juan = this.findById(id);
        if(!(Juan == null)) {
            entityManager.remove(Juan);
            return true;
        }
        return false;
    }
}