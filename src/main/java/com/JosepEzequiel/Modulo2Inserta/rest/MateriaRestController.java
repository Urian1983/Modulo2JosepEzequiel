package com.JosepEzequiel.Modulo2Inserta.rest;

import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.dao.MateriaDAO;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class MateriaRestController {

    private final MateriaDAO materiaDAO;

    public MateriaRestController(MateriaDAO materiaDAO) {
        this.materiaDAO = materiaDAO;
    }

    @PostMapping("/materias")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Materia> createMateria(@RequestBody Materia materia) {
        return materiaDAO.createMateria(materia);
        }

    @PutMapping("/materias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Materia> updateMateria(@PathVariable int id, @RequestBody Materia materia) {
        materia.setId(id);
        return materiaDAO.updateMateria(materia);
        }

    @GetMapping("/materias")
    @ResponseStatus(HttpStatus.OK)
    public List<Materia> findAllMaterias() {
        List<Materia> materias = materiaDAO.findAllMaterias();
        if (materias.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return materias;
    }

    @GetMapping("/materias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Materia> findMateria(@PathVariable int id) {
        Optional<Materia> materia = materiaDAO.findMateriaById(id);
        if(materia.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return materia;
    }

    @GetMapping("materias/profesor/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Profesor> findProfesor(@PathVariable int id) {
        Optional<Profesor> profesor = materiaDAO.findProfesorById(id);

        if(profesor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return profesor;
    }

    @DeleteMapping("/materias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMateria(@PathVariable int id) {
        if(!(materiaDAO.deleteMateria(id))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    }

