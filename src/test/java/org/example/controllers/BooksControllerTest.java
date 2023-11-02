package org.example.controllers;

import org.example.services.BooksService;
import org.example.services.DTO.BookRequestDTO;
import org.example.services.DTO.BookResponseDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BooksControllerTest {

    @Mock
    BooksService booksService;

    @InjectMocks
    BooksController booksController;

    @BeforeEach
    void setUp() {
        booksController = new BooksController(booksService);
    }

    @Test
    void getBooksTest() {
        ArrayList<BookResponseDTO> dtos = new ArrayList<>(List.of(
                new BookResponseDTO(UUID.randomUUID(), "Title1", 1900, new PersonResponseDTO()),
                new BookResponseDTO(UUID.randomUUID(), "Title2", 1900, new PersonResponseDTO())
        ));

        when(booksService.findAll()).thenReturn(dtos);
        ResponseEntity<List<BookResponseDTO>> books = booksController.getBooks();

        Assertions.assertEquals(HttpStatus.OK, books.getStatusCode());
        Assertions.assertEquals(dtos, books.getBody());
    }

    @Test
    void getNotFoundBooksTest() {
        when(booksService.findAll()).thenReturn(null);
        ResponseEntity<List<BookResponseDTO>> books = booksController.getBooks();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, books.getStatusCode());
        Assertions.assertNull(books.getBody());
    }

    @Test
    void getBookTest() {
        UUID id = UUID.randomUUID();
        BookResponseDTO responseDTO = new BookResponseDTO(id, "Title", 1900, new PersonResponseDTO());

        when(booksService.findById(id)).thenReturn(responseDTO);
        ResponseEntity<BookResponseDTO> book = booksController.getBook(id);

        Assertions.assertEquals(HttpStatus.OK, book.getStatusCode());
        Assertions.assertEquals(responseDTO, book.getBody());
    }

    @Test
    void getBookNotAcceptableTest() {
        UUID id = UUID.randomUUID();

        when(booksService.findById(id)).thenReturn(null);
        ResponseEntity<BookResponseDTO> book = booksController.getBook(id);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, book.getStatusCode());
        Assertions.assertNull(book.getBody());
    }

    @Test
    void createTest() {
        UUID id = UUID.randomUUID();
        BookRequestDTO requestDTO = new BookRequestDTO("Title", 1900, "", new ArrayList<>());
        BookResponseDTO responseDTO = new BookResponseDTO(id, "Title", 1900, new PersonResponseDTO());

        when(booksService.create(requestDTO)).thenReturn(responseDTO);
        ResponseEntity<BookResponseDTO> create = booksController.create(requestDTO);

        Assertions.assertEquals(HttpStatus.CREATED, create.getStatusCode());
        Assertions.assertEquals(responseDTO, create.getBody());
    }

    @Test
    void updateTest() {
        UUID id = UUID.randomUUID();
        BookRequestDTO requestDTO = new BookRequestDTO("Title", 1900, "", new ArrayList<>());
        BookResponseDTO responseDTO = new BookResponseDTO(id, "Title", 1900, new PersonResponseDTO());

        when(booksService.updateById(id, requestDTO)).thenReturn(responseDTO);
        ResponseEntity<BookResponseDTO> update = booksController.update(id, requestDTO);

        Assertions.assertEquals(HttpStatus.OK, update.getStatusCode());
        Assertions.assertEquals(responseDTO, update.getBody());
    }

    @Test
    void updateBadRequestTest() {
        UUID id = UUID.randomUUID();
        BookRequestDTO requestDTO = new BookRequestDTO("Title", 1900, "", new ArrayList<>());

        when(booksService.updateById(id, requestDTO)).thenReturn(null);
        ResponseEntity<BookResponseDTO> update = booksController.update(id, requestDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, update.getStatusCode());
        Assertions.assertNull(update.getBody());
    }

    @Test
    void delete() {
        UUID id = UUID.randomUUID();

        when(booksService.delete(id)).thenReturn(true);
        HttpStatus delete = booksController.delete(id);

        Assertions.assertEquals(HttpStatus.OK, delete);
    }

    @Test
    void deleteNotFoundTest() {
        UUID id = UUID.randomUUID();

        when(booksService.delete(id)).thenReturn(false);
        HttpStatus delete = booksController.delete(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, delete);
    }
}