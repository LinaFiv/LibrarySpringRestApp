package org.example.services;

import org.example.entities.Book;
import org.example.entities.Person;
import org.example.repositories.PeopleRepository;
import org.example.services.DTO.PersonRequestDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.example.services.mappers.PersonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {


    @Mock
    PeopleRepository peopleRepository;

    @Mock
    PersonMapper personMapper;

    @Mock
    BooksService booksService;

    @InjectMocks
    PeopleService peopleService;

    @BeforeEach
    void setUp() {
        peopleService = new PeopleService(peopleRepository, personMapper, booksService);
    }

    @Test
    void findDTOByIdTest() {
        Person person = new Person(UUID.randomUUID(), "Person", 1990);
        PersonResponseDTO responseDTO = new PersonResponseDTO(UUID.randomUUID(), "Person", 1990);
        when(peopleRepository.findById(person.getId())).thenReturn(Optional.of(person));
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);
        PersonResponseDTO byId = peopleService.findById(person.getId());

        verify(peopleRepository, times(1)).findById(person.getId());
        verify(personMapper, times(1)).toResponseDTO(person);
        Assertions.assertEquals(responseDTO, byId);
    }

    @Test
    void findPersonByIdTest() {
        String id = "108a3313-60a4-4bab-86e8-66c0359076f5";
        UUID uuid = UUID.fromString(id);
        Person person = new Person(uuid, "Person", 1990);

        when(peopleRepository.findById(uuid)).thenReturn(Optional.of(person));
        Person foundPerson = peopleService.findById(id);

        Assertions.assertEquals(person, foundPerson);
    }

    @Test
    void findAllTest() {
        List<Person> people = new ArrayList<>(List.of(
                new Person(UUID.randomUUID(), "Person1", 1990),
                new Person(UUID.randomUUID(), "Person2", 1990),
                new Person(UUID.randomUUID(), "Person3", 1990)
        ));
        ArrayList<PersonResponseDTO> dtos = new ArrayList<>(List.of(
                new PersonResponseDTO(UUID.randomUUID(), "Person1", 1990),
                new PersonResponseDTO(UUID.randomUUID(), "Person2", 1990),
                new PersonResponseDTO(UUID.randomUUID(), "Person3", 1990)
        ));

        when(peopleRepository.findAll()).thenReturn(people);
        when(personMapper.toDTOList(people)).thenReturn(dtos);
        List<PersonResponseDTO> all = peopleService.findAll();

        verify(peopleRepository, times(1)).findAll();
        verify(personMapper, times(1)).toDTOList(people);
        Assertions.assertEquals(dtos, all);
    }

    @Test
    void createTest() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        ArrayList<Book> books = new ArrayList<>(List.of(
                new Book(UUID.fromString(id1), "Title1", 1900),
                new Book(UUID.fromString(id2), "Title2", 1900)
        ));
        PersonRequestDTO requestPerson = new PersonRequestDTO("PersonCreate", 1990, new ArrayList<>(List.of(id1, id2)));
        Person person = new Person(UUID.randomUUID(), requestPerson.getFullName(), requestPerson.getYearOfBirth());
        PersonResponseDTO responsePerson = new PersonResponseDTO(person.getId(), person.getFullName(), person.getYearOfBirth());

        when(booksService.findAllById(requestPerson.getBooksId())).thenReturn(books);
        when(personMapper.toEntity(requestPerson)).thenReturn(person);
        when(peopleRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responsePerson);
        PersonResponseDTO create = peopleService.create(requestPerson);

        verify(booksService, times(1)).findAllById(requestPerson.getBooksId());
        verify(personMapper, times(1)).toEntity(requestPerson);
        verify(peopleRepository, times(1)).save(person);
        verify(personMapper, times(1)).toResponseDTO(person);
        Assertions.assertEquals(responsePerson, create);
    }

    @Test
    void updateByFindIdTest() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        UUID id = UUID.randomUUID();

        ArrayList<Book> books = new ArrayList<>(List.of(
                new Book(UUID.fromString(id1), "Title1", 1900),
                new Book(UUID.fromString(id2), "Title2", 1900)
        ));
        Person person = new Person(id, "Person", 2000, new ArrayList<>());
        PersonRequestDTO requestPerson = new PersonRequestDTO("UpdateName", 1990, new ArrayList<>(List.of(id1, id2)));
        Person updatePerson = new Person(id, requestPerson.getFullName(), requestPerson.getYearOfBirth(), new ArrayList<>());
        PersonResponseDTO responsePerson = new PersonResponseDTO(id, updatePerson.getFullName(), updatePerson.getYearOfBirth());

        when(personMapper.toEntity(requestPerson)).thenReturn(updatePerson);
        when(peopleRepository.findById(id)).thenReturn(Optional.of(person));
        when(booksService.findAllById(requestPerson.getBooksId())).thenReturn(books);
        when(personMapper.toResponseDTO(peopleRepository.save(person))).thenReturn(responsePerson);
        PersonResponseDTO updateById = peopleService.updateById(id, requestPerson);

        verify(personMapper, times(1)).toEntity(requestPerson);
        verify(peopleRepository, times(1)).findById(id);
        verify(booksService, times(1)).findAllById(requestPerson.getBooksId());
        verify(personMapper, times(1)).toResponseDTO(peopleRepository.save(person));
        Assertions.assertEquals(responsePerson, updateById);
    }

    @Test
    void updateByNotFindIdTest() {
        UUID id = UUID.randomUUID();
        PersonRequestDTO requestPerson = new PersonRequestDTO("UpdateName", 1990, new ArrayList<>());
        Person updatePerson = new Person(id, requestPerson.getFullName(), requestPerson.getYearOfBirth(), new ArrayList<>());

        when(personMapper.toEntity(requestPerson)).thenReturn(updatePerson);
        when(peopleRepository.findById(id)).thenReturn(Optional.empty());
        PersonResponseDTO updateById = peopleService.updateById(id, requestPerson);

        verify(personMapper, times(1)).toEntity(requestPerson);
        verify(peopleRepository, times(1)).findById(id);
        Assertions.assertNull(updateById);
    }

    @Test
    void deleteTrueTest() {
        UUID id = UUID.randomUUID();
        when(peopleRepository.findById(id)).thenReturn(Optional.empty());
        boolean delete = peopleService.delete(id);

        Assertions.assertTrue(delete);
    }

    @Test
    void deleteFalseTest() {
        UUID id = UUID.randomUUID();
        Person person = new Person();
        when(peopleRepository.findById(id)).thenReturn(Optional.of(person));
        boolean delete = peopleService.delete(id);

        Assertions.assertFalse(delete);
    }
}