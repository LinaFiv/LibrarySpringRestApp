package org.example.controllers;

import org.example.services.DTO.PersonRequestDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonResponseDTO>> getPeople() {
        List<PersonResponseDTO> people = peopleService.findAll();
        if (people != null) {
            return new ResponseEntity<>(people, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonResponseDTO> getPerson(@PathVariable UUID id) {
        PersonResponseDTO person = peopleService.findById(id);
        if (person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PersonResponseDTO> create(@RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO createPerson = peopleService.create(personRequestDTO);
        return new ResponseEntity<>(createPerson, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PersonResponseDTO> update(@PathVariable UUID id, @RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO updatePerson = peopleService.updateById(id, personRequestDTO);
        if (updatePerson != null) {
            return new ResponseEntity<>(updatePerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus delete(@PathVariable UUID id) {
        boolean delete = peopleService.delete(id);
        if (delete) return HttpStatus.OK;
        return HttpStatus.NOT_FOUND;
    }
}
