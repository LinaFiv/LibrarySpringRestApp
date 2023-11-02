package org.example.controllers;

import org.example.services.BooksService;
import org.example.services.DTO.BookRequestDTO;
import org.example.services.DTO.BookResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;

    @Autowired
    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookResponseDTO>> getBooks() {
        List<BookResponseDTO> books = booksService.findAll();
        if (books != null) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable UUID id) {
        BookResponseDTO book = booksService.findById(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO createBook = booksService.create(bookRequestDTO);
        return new ResponseEntity<>(createBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BookResponseDTO> update(@PathVariable UUID id, @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO updateBook = booksService.updateById(id, bookRequestDTO);
        if (updateBook != null) {
            return new ResponseEntity<>(updateBook, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus delete(@PathVariable UUID id) {
        boolean delete = booksService.delete(id);
        if (delete) return HttpStatus.OK;
        return HttpStatus.NOT_FOUND;
    }
}
