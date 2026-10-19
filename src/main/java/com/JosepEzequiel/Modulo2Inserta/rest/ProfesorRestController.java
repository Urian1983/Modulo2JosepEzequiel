package com.JosepEzequiel.Modulo2Inserta.rest;

import com.JosepEzequiel.Modulo2Inserta.dao.ProfesorDAO;
import com.JosepEzequiel.Modulo2Inserta.entity.Materia;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ProfesorRestController {

    private ProfesorDAO profesorDAO;

    @Autowired
    public ProfesorRestController(ProfesorDAO profesorDAO) {
        this.profesorDAO = profesorDAO;
    }

    @GetMapping("/profesores")
    public List<Profesor> findAll() {
        return profesorDAO.findAll();
    }

    @GetMapping("/profesores/{profesorId}")
    public Profesor getProfesor(@PathVariable int profesorId) {

        Profesor profesor = this.profesorDAO.findById(profesorId);

        if (profesor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return profesor;
    }

    @PostMapping("/profesores") 
    @ResponseStatus(HttpStatus.CREATED)
    public Profesor addProfesores(@RequestBody Profesor theProfesores) {
        profesorDAO.save(theProfesores);
        return theProfesores;
    }

    @PutMapping("/profesores")
    public Profesor updateProfesor(@RequestBody Profesor theProfesor) {
        this.profesorDAO.update(theProfesor);
        return theProfesor;
    }

    @DeleteMapping("/profesores/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfesor(@PathVariable int id) {
        if (!(profesorDAO.delete(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profesores/{id}/materias")
    @ResponseStatus(HttpStatus.OK)
    public List<Materia> findMaterias(@PathVariable  int id) {
        return profesorDAO.findMaterias(id);
    }
   
    }


