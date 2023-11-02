package org.example.controllers;

import org.example.services.AuthorsService;
import org.example.services.DTO.AuthorRequestDTO;
import org.example.services.DTO.AuthorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorsService authorsService;

    @Autowired
    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthorResponseDTO>> getAuthors() {
        List<AuthorResponseDTO> authors = authorsService.findAll();
        if (authors != null) {
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable UUID id) {
        AuthorResponseDTO author = authorsService.findById(id);
        if (author != null) {
            return new ResponseEntity<>(author, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorResponseDTO> create(@RequestBody AuthorRequestDTO authorRequestDTO) {
        AuthorResponseDTO createAuthor = authorsService.create(authorRequestDTO);
        return new ResponseEntity<>(createAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthorResponseDTO> update(@PathVariable UUID id, @RequestBody AuthorRequestDTO authorRequestDTO) {
        AuthorResponseDTO updateAuthor = authorsService.updateById(id, authorRequestDTO);
        if (updateAuthor != null) {
            return new ResponseEntity<>(updateAuthor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus delete(@PathVariable UUID id) {
        boolean delete = authorsService.delete(id);
        if (delete) return HttpStatus.OK;
        return HttpStatus.NOT_FOUND;
    }
}
