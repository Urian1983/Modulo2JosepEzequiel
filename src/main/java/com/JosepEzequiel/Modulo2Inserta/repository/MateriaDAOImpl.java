package com.JosepEzequiel.Modulo2Inserta.repository;

import com.JosepEzequiel.Modulo2Inserta.model.Materia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MateriaDAOImpl implements MateriaDAO {

    private final EntityManager entityManager;

    public MateriaDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void createMateria(Materia materia) {
        entityManager.persist(materia);
    }

    @Override
    @Transactional
    public void updateMateria(Materia materia, int id) {
        Materia materiaToUpdate = entityManager.find(Materia.class, id);

        if(materiaToUpdate != null) {
            materiaToUpdate.setNombre(materia.getNombre());
            materiaToUpdate.setHorasCredito(materia.getHorasCredito());
            materiaToUpdate.setNumCreditos(materia.getNumCreditos());
        }
    }

    @Override
    @Transactional
    public void deleteMateria(int id) {
        entityManager.remove(entityManager.find(Materia.class, id));
    }

    @Override
    public List<Materia> findAllMaterias() {
        TypedQuery<Materia> ListAllQuery;
        ListAllQuery = entityManager.createQuery("FROM Materia", Materia.class);
        return ListAllQuery.getResultList();
    }

    //He tenido que consultar a la IA para poder devolverlo como un Optional
    @Override
    public Optional<Materia> findMateriaById(int id) {
        return Optional.ofNullable(
                entityManager.find(Materia.class, id)
        );
    }
    @Override
    public Optional<Materia> findMateriaByNombre(String nombre) {
        TypedQuery<Materia> theQuery = entityManager.createQuery(
                "FROM Materia WHERE nombre = :nombre", Materia.class
        );
        return theQuery.getResultStream()
                .findFirst();
    }
}
