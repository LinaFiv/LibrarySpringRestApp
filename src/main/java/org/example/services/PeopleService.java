package org.example.services;

import org.example.entities.Book;
import org.example.entities.Person;
import org.example.repositories.PeopleRepository;
import org.example.services.DTO.PersonRequestDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.example.services.mappers.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final PersonMapper personMapper;
    private final BooksService booksService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PersonMapper personMapper, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.personMapper = personMapper;
        this.booksService = booksService;
    }

    public PersonResponseDTO findById(UUID id) {
        Person person = peopleRepository.findById(id).orElse(null);
        return personMapper.toResponseDTO(person);
    }

    public Person findById(String id) {
        UUID uuid = UUID.fromString(id);
        return peopleRepository.findById(uuid).orElse(null);
    }

    public List<PersonResponseDTO> findAll() {
        List<Person> people = peopleRepository.findAll();
        return personMapper.toDTOList(people);
    }

    public PersonResponseDTO create(PersonRequestDTO person) {
        List<Book> books = booksService.findAllById(person.getBooksId());
        Person personEntity = personMapper.toEntity(person);
        Person createPerson = peopleRepository.save(personEntity);
        booksService.updatePersonId(books, createPerson);
        return personMapper.toResponseDTO(createPerson);
    }

    public PersonResponseDTO updateById(UUID id, PersonRequestDTO updatePerson) {
        Person updatePersonEntity = personMapper.toEntity(updatePerson);
        Person person = peopleRepository.findById(id).orElse(null);
        List<Book> books = booksService.findAllById(updatePerson.getBooksId());
        if (person != null) {
            if (books.equals(person.getBooks())) {
                booksService.updateNullPersonId(person.getBooks());
                booksService.updatePersonId(books, person);
            }
            person.setFullName(updatePersonEntity.getFullName());
            person.setYearOfBirth(updatePersonEntity.getYearOfBirth());
            person.setBooks(updatePersonEntity.getBooks());
            return personMapper.toResponseDTO(peopleRepository.save(person));
        }
        return null;
    }

    public boolean delete(UUID id) {
        Person person = peopleRepository.findById(id).orElse(null);
        if (person != null) {
            List<Book> books = person.getBooks();
            booksService.updateNullPersonId(books);
        }
        peopleRepository.deleteById(id);
        return peopleRepository.findById(id).isEmpty();
    }
}
