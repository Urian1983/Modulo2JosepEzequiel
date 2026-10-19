package com.JosepEzequiel.Modulo2Inserta.dao;

import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
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
    public Optional<Materia> save(Materia materia) {
        entityManager.persist(materia);
        return this.findById(materia.getId());
    }

    @Override
    @Transactional
    public Optional<Materia> update(Materia materia) {
        if(this.findById(materia.getId()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(materia);
        }

    @Override
    @Transactional
    public boolean delete(int id) {
        Optional<Materia> materiaToRemove = this.findById(id);
        if(materiaToRemove.isPresent()) {
            entityManager.remove(materiaToRemove.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Materia> findAll() {
        TypedQuery<Materia> query;
        query = entityManager.createQuery("FROM Materia", Materia.class);
        return query.getResultList();
    }

    @Override
    public Optional<Materia> findById(int id) {
        return Optional.ofNullable(
                entityManager.find(Materia.class, id)
        );
    }
}
