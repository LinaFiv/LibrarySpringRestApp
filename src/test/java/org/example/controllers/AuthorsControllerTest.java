package org.example.controllers;

import org.example.services.AuthorsService;
import org.example.services.DTO.AuthorRequestDTO;
import org.example.services.DTO.AuthorResponseDTO;
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
class AuthorsControllerTest {

    @Mock
    AuthorsService authorsService;

    @InjectMocks
    AuthorsController authorsController;

    @BeforeEach
    void setUp() {
        authorsController = new AuthorsController(authorsService);
    }

    @Test
    void getAuthorsTest() {
        ArrayList<AuthorResponseDTO> dtos = new ArrayList<>(List.of(
                new AuthorResponseDTO(UUID.randomUUID(), "Author1"),
                new AuthorResponseDTO(UUID.randomUUID(), "Author2")
        ));

        when(authorsService.findAll()).thenReturn(dtos);
        ResponseEntity<List<AuthorResponseDTO>> authors = authorsController.getAuthors();

        Assertions.assertEquals(HttpStatus.OK, authors.getStatusCode());
        Assertions.assertEquals(dtos, authors.getBody());
    }

    @Test
    void getNotFoundAuthorsTest() {
        when(authorsService.findAll()).thenReturn(null);
        ResponseEntity<List<AuthorResponseDTO>> authors = authorsController.getAuthors();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, authors.getStatusCode());
        Assertions.assertNull(authors.getBody());
    }

    @Test
    void getAuthorTest() {
        UUID id = UUID.randomUUID();
        AuthorResponseDTO responseDTO = new AuthorResponseDTO(id, "Author");

        when(authorsService.findById(id)).thenReturn(responseDTO);
        ResponseEntity<AuthorResponseDTO> author = authorsController.getAuthor(id);

        Assertions.assertEquals(HttpStatus.OK, author.getStatusCode());
        Assertions.assertEquals(responseDTO, author.getBody());
    }

    @Test
    void getAuthorNotAcceptableTest() {
        UUID id = UUID.randomUUID();

        when(authorsService.findById(id)).thenReturn(null);
        ResponseEntity<AuthorResponseDTO> author = authorsController.getAuthor(id);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, author.getStatusCode());
        Assertions.assertNull(author.getBody());
    }

    @Test
    void createTest() {
        UUID id = UUID.randomUUID();
        AuthorRequestDTO requestDTO = new AuthorRequestDTO("Title");
        AuthorResponseDTO responseDTO = new AuthorResponseDTO(id, "Author");

        when(authorsService.create(requestDTO)).thenReturn(responseDTO);
        ResponseEntity<AuthorResponseDTO> create = authorsController.create(requestDTO);

        Assertions.assertEquals(HttpStatus.CREATED, create.getStatusCode());
        Assertions.assertEquals(responseDTO, create.getBody());
    }

    @Test
    void updateTest() {
        UUID id = UUID.randomUUID();
        AuthorRequestDTO requestDTO = new AuthorRequestDTO("Author");
        AuthorResponseDTO responseDTO = new AuthorResponseDTO(id, "Author");

        when(authorsService.updateById(id, requestDTO)).thenReturn(responseDTO);
        ResponseEntity<AuthorResponseDTO> update = authorsController.update(id, requestDTO);

        Assertions.assertEquals(HttpStatus.OK, update.getStatusCode());
        Assertions.assertEquals(responseDTO, update.getBody());
    }

    @Test
    void updateBadRequestTest() {
        UUID id = UUID.randomUUID();
        AuthorRequestDTO requestDTO = new AuthorRequestDTO("Author");

        when(authorsService.updateById(id, requestDTO)).thenReturn(null);
        ResponseEntity<AuthorResponseDTO> update = authorsController.update(id, requestDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, update.getStatusCode());
        Assertions.assertNull(update.getBody());
    }

    @Test
    void deleteTest() {
        UUID id = UUID.randomUUID();

        when(authorsService.delete(id)).thenReturn(true);
        HttpStatus delete = authorsController.delete(id);

        Assertions.assertEquals(HttpStatus.OK, delete);
    }

    @Test
    void deleteNotFoundTest() {
        UUID id = UUID.randomUUID();

        when(authorsService.delete(id)).thenReturn(false);
        HttpStatus delete = authorsController.delete(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, delete);
    }
}