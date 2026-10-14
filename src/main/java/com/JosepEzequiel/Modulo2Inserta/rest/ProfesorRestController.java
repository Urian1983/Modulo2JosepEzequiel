package com.JosepEzequiel.Modulo2Inserta.rest;

import com.JosepEzequiel.Modulo2Inserta.dao.ProfesorRepository;
import com.JosepEzequiel.Modulo2Inserta.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorRestController {

    @Autowired
    private ProfesorRepository profesorRepository;

    public List<Profesor> listar() {
        return profesorRepository.findAll();
    }

    @PostMapping
    public Profesor guardar(@RequestBody Profesor profesor) {
        return profesorRepository.save(profesor);
    }
}
