package com.JosepEzequiel.Modulo2Inserta.rest;

import com.JosepEzequiel.Modulo2Inserta.dao.MateriaDAO;
import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api")
public class MateriaRestController {

    MateriaDAO materiaDAO;

    @Autowired
    public MateriaRestController(MateriaDAO materiaDAO) {
        this.materiaDAO = materiaDAO;
    }

    @PostMapping("/materias")
    @ResponseStatus(HttpStatus.CREATED)
    public void insert(@RequestBody Materia materia) {
        if(materiaDAO.findById(materia.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Materia ya existente");
        }
        materiaDAO.save(materia);
    }

    @PutMapping("/materias")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Materia materia) {
        if(materiaDAO.findById(materia.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada, no se puede actualizar");
        }
        materiaDAO.update(materia);
    }

    @GetMapping("/materias")
    @ResponseStatus(HttpStatus.OK)
    public List<Materia> findAll() {
        return materiaDAO.findAll();
    }

    @GetMapping("/materias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Materia findById(@PathVariable int id) {
        return materiaDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Materia no encontrada"));
    }

    @DeleteMapping("/materias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        if(materiaDAO.findById(id).isPresent()) {
            materiaDAO.delete(id);
        }
    }

    @GetMapping("/materia/{materiaId}/profesor")
    @ResponseStatus(HttpStatus.OK)
    public Profesor findByProfesor(@PathVariable int materiaId) {
        Materia materia = materiaDAO.findById(materiaId)
                .orElseThrow(() -> new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Materia no encontrada"));

        if(materia.getProfesor() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado");
        }
        return materia.getProfesor();
    }
}
