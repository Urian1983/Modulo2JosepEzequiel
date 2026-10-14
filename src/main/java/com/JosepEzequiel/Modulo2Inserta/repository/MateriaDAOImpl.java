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
    public Optional<Materia> createMateria(Materia materia) {
        entityManager.persist(materia);
        return this.findMateriaById(materia.getId());
    }

    @Override
    @Transactional
    public Optional<Materia> updateMateria(Materia materia) {
        Optional<Materia> materiaToUpdate = this.findMateriaById(materia.getId());
        if (materiaToUpdate.isPresent()) {
            Materia updatedMateria = entityManager.merge(materia);
            return Optional.ofNullable(updatedMateria);
        }
        return Optional.empty();
        }

    @Override
    @Transactional
    public boolean deleteMateria(int id) {
        Optional<Materia> materiaToRemove = this.findMateriaById(id);
        if(materiaToRemove.isPresent()) {
            entityManager.remove(materiaToRemove.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Materia> findAllMaterias() {
        TypedQuery<Materia> query;
        query = entityManager.createQuery("FROM Materia", Materia.class);
        return query.getResultList();
    }

    @Override
    public Optional<Materia> findMateriaById(int id) {
        return Optional.ofNullable(
                entityManager.find(Materia.class, id)
        );
    }
    @Override
    public Optional<Materia> findMateriaByNombre(String nombre) {
        TypedQuery<Materia> query = entityManager.createQuery(
                "FROM Materia WHERE nombre = :nombre", Materia.class
        );
        return query.setParameter("nombre", nombre)
                .getResultStream()
                .findFirst();
    }
}
