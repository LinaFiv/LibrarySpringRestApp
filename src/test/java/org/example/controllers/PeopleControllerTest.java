package org.example.controllers;

import org.example.services.DTO.PersonRequestDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.example.services.PeopleService;
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
class PeopleControllerTest {

    @Mock
    PeopleService peopleService;

    @InjectMocks
    PeopleController peopleController;

    @BeforeEach
    void setUp() {
        peopleController = new PeopleController(peopleService);
    }

    @Test
    void getPeopleTest() {
        ArrayList<PersonResponseDTO> dtos = new ArrayList<>(List.of(
                new PersonResponseDTO(UUID.randomUUID(), "Person1", 1990),
                new PersonResponseDTO(UUID.randomUUID(), "Person2", 1990)
        ));

        when(peopleService.findAll()).thenReturn(dtos);
        ResponseEntity<List<PersonResponseDTO>> people = peopleController.getPeople();

        Assertions.assertEquals(HttpStatus.OK, people.getStatusCode());
        Assertions.assertEquals(dtos, people.getBody());
    }

    @Test
    void getNotFoundPeopleTest() {
        when(peopleService.findAll()).thenReturn(null);
        ResponseEntity<List<PersonResponseDTO>> people = peopleController.getPeople();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, people.getStatusCode());
        Assertions.assertNull(people.getBody());
    }

    @Test
    void getPersonTest() {
        UUID id = UUID.randomUUID();
        PersonResponseDTO responseDTO = new PersonResponseDTO(id, "Person", 1990);

        when(peopleService.findById(id)).thenReturn(responseDTO);
        ResponseEntity<PersonResponseDTO> person = peopleController.getPerson(id);

        Assertions.assertEquals(HttpStatus.OK, person.getStatusCode());
        Assertions.assertEquals(responseDTO, person.getBody());
    }

    @Test
    void getPersonNotAcceptableTest() {
        UUID id = UUID.randomUUID();

        when(peopleService.findById(id)).thenReturn(null);
        ResponseEntity<PersonResponseDTO> person = peopleController.getPerson(id);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, person.getStatusCode());
        Assertions.assertNull(person.getBody());
    }

    @Test
    void createTest() {
        UUID id = UUID.randomUUID();
        PersonRequestDTO requestDTO = new PersonRequestDTO("Person", 1990, new ArrayList<>());
        PersonResponseDTO responseDTO = new PersonResponseDTO(id, "Person", 1990);

        when(peopleService.create(requestDTO)).thenReturn(responseDTO);
        ResponseEntity<PersonResponseDTO> create = peopleController.create(requestDTO);

        Assertions.assertEquals(HttpStatus.CREATED, create.getStatusCode());
        Assertions.assertEquals(responseDTO, create.getBody());
    }

    @Test
    void updateTest() {
        UUID id = UUID.randomUUID();
        PersonRequestDTO requestDTO = new PersonRequestDTO("Person", 1990, new ArrayList<>());
        PersonResponseDTO responseDTO = new PersonResponseDTO(id, "Person", 1990);

        when(peopleService.updateById(id, requestDTO)).thenReturn(responseDTO);
        ResponseEntity<PersonResponseDTO> update = peopleController.update(id, requestDTO);

        Assertions.assertEquals(HttpStatus.OK, update.getStatusCode());
        Assertions.assertEquals(responseDTO, update.getBody());
    }

    @Test
    void updateBadRequestTest() {
        UUID id = UUID.randomUUID();
        PersonRequestDTO requestDTO = new PersonRequestDTO("Person", 1990, new ArrayList<>());

        when(peopleService.updateById(id, requestDTO)).thenReturn(null);
        ResponseEntity<PersonResponseDTO> update = peopleController.update(id, requestDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, update.getStatusCode());
        Assertions.assertNull(update.getBody());
    }

    @Test
    void deleteTest() {
        UUID id = UUID.randomUUID();

        when(peopleService.delete(id)).thenReturn(true);
        HttpStatus delete = peopleController.delete(id);

        Assertions.assertEquals(HttpStatus.OK, delete);
    }

    @Test
    void deleteNotFoundTest() {
        UUID id = UUID.randomUUID();

        when(peopleService.delete(id)).thenReturn(false);
        HttpStatus delete = peopleController.delete(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, delete);
    }
}