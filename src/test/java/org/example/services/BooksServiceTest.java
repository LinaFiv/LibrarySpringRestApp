package org.example.services;

import org.example.entities.Author;
import org.example.entities.Book;
import org.example.entities.Person;
import org.example.repositories.BooksRepository;
import org.example.services.DTO.BookRequestDTO;
import org.example.services.DTO.BookResponseDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.example.services.mappers.BookMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    BooksRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Mock
    AuthorsService authorsService;

    @Mock
    PeopleService peopleService;

    @InjectMocks
    BooksService booksService;

    @BeforeEach
    void setUp() {
        booksService = new BooksService(bookRepository, bookMapper, authorsService, peopleService);
    }

    @Test
    void findByIdTest() {
        Book book = new Book(UUID.randomUUID(), "Title", 1900, new Person(), new ArrayList<>());
        BookResponseDTO responseDTO = new BookResponseDTO(UUID.randomUUID(), "Title", 1900, new PersonResponseDTO());
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toResponseDTO(book)).thenReturn(responseDTO);
        BookResponseDTO byId = booksService.findById(book.getId());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookMapper, times(1)).toResponseDTO(book);
        Assertions.assertEquals(responseDTO, byId);
    }

    @Test
    void findAllTest() {
        List<Book> books = new ArrayList<>(List.of(
                new Book(UUID.randomUUID(), "Title1", 1900, new Person(), new ArrayList<>()),
                new Book(UUID.randomUUID(), "Title2", 1900, new Person(), new ArrayList<>()),
                new Book(UUID.randomUUID(), "Title3", 1900, new Person(), new ArrayList<>())
        ));
        ArrayList<BookResponseDTO> dtos = new ArrayList<>(List.of(
                new BookResponseDTO(UUID.randomUUID(), "Title1", 1900, new PersonResponseDTO()),
                new BookResponseDTO(UUID.randomUUID(), "Title2", 1900, new PersonResponseDTO()),
                new BookResponseDTO(UUID.randomUUID(), "Title3", 1900, new PersonResponseDTO())
        ));

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDTOList(books)).thenReturn(dtos);
        List<BookResponseDTO> all = booksService.findAll();

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDTOList(books);
        Assertions.assertEquals(dtos, all);
    }

    @Test
    void findAllByIdTest() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        List<String> ids = new ArrayList<>(List.of(id1, id2));
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        List<Book> books = new ArrayList<>(List.of(
                new Book(UUID.fromString(id1), "Title1", 1900),
                new Book(UUID.fromString(id2), "Title2", 1900)
        ));
        when(bookRepository.findAllById(uuids)).thenReturn(books);
        List<Book> all = booksService.findAllById(ids);

        verify(bookRepository, times(1)).findAllById(uuids);
        Assertions.assertEquals(books, all);
    }

    @Test
    void createTest() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        ArrayList<Author> authors = new ArrayList<>(List.of(
                new Author(UUID.fromString(id1), "Author1"),
                new Author(UUID.fromString(id2), "Author2")
        ));
        BookRequestDTO requestBook = new BookRequestDTO("TitleCreate", 1900, "", new ArrayList<>(List.of(id1, id2)));
        Book book = new Book(UUID.randomUUID(), requestBook.getTitle(), requestBook.getYear(), new Person(), authors);
        BookResponseDTO responseBook = new BookResponseDTO(book.getId(), book.getTitle(), book.getYear(), new PersonResponseDTO());

        when(authorsService.findAllById(requestBook.getAuthorsId())).thenReturn(authors);
        when(peopleService.findById(requestBook.getOwnerId())).thenReturn(null);
        when(bookMapper.toEntity(requestBook)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponseDTO(book)).thenReturn(responseBook);
        BookResponseDTO create = booksService.create(requestBook);

        verify(authorsService, times(1)).findAllById(requestBook.getAuthorsId());
        verify(bookMapper, times(1)).toEntity(requestBook);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponseDTO(book);
        Assertions.assertEquals(responseBook, create);
    }

    @Test
    void updatePersonIdTest() {
        List<Book> books = new ArrayList<>(List.of(
                new Book(UUID.randomUUID(), "Title1", 1900),
                new Book(UUID.randomUUID(), "Title2", 1900)
        ));
        Person person = new Person(UUID.randomUUID(), "Person", 1990);

        booksService.updatePersonId(books, person);

        Assertions.assertEquals(person, books.get(0).getOwner());
        Assertions.assertEquals(person, books.get(1).getOwner());

    }

    @Test
    void updateNullPersonId() {
        Person person = new Person(UUID.randomUUID(), "Test", 1990);
        List<Book> books = new ArrayList<>(List.of(
                new Book(UUID.randomUUID(), "Title1", 1900, person),
                new Book(UUID.randomUUID(), "Title2", 1900, person)
        ));

        booksService.updateNullPersonId(books);

        Assertions.assertNull(books.get(0).getOwner());
        Assertions.assertNull(books.get(1).getOwner());

    }

    @Test
    void updateByFindIdTest() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        UUID id = UUID.randomUUID();

        ArrayList<Author> authors = new ArrayList<>(List.of(
                new Author(UUID.fromString(id1), "Author1"),
                new Author(UUID.fromString(id2), "Author2")
        ));
        Person person = new Person(UUID.randomUUID(), "Test", 1990);
        Book book = new Book(UUID.randomUUID(), "Title", 1900, new Person(), new ArrayList<>());
        BookRequestDTO requestBook = new BookRequestDTO("UpdateName", 1900, String.valueOf(person.getId()), new ArrayList<>(List.of(id1, id2)));
        Book updatePerson = new Book(id, requestBook.getTitle(), requestBook.getYear(), new Person(), new ArrayList<>());
        BookResponseDTO responseBook = new BookResponseDTO(id, updatePerson.getTitle(), updatePerson.getYear(), new PersonResponseDTO());

        when(bookMapper.toEntity(requestBook)).thenReturn(updatePerson);
        when(peopleService.findById(requestBook.getOwnerId())).thenReturn(person);
        when(authorsService.findAllById(requestBook.getAuthorsId())).thenReturn(authors);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toResponseDTO(bookRepository.save(book))).thenReturn(responseBook);
        BookResponseDTO updateById = booksService.updateById(id, requestBook);

        verify(bookMapper, times(1)).toEntity(requestBook);
        verify(peopleService, times(1)).findById(requestBook.getOwnerId());
        verify(authorsService, times(1)).findAllById(requestBook.getAuthorsId());
        verify(bookRepository, times(1)).findById(id);
        verify(bookMapper, times(1)).toResponseDTO(bookRepository.save(book));
        Assertions.assertEquals(responseBook, updateById);
    }

    @Test
    void updateByNotFindIdTest() {
        UUID id = UUID.randomUUID();
        BookRequestDTO requestBook = new BookRequestDTO("UpdateName", 1900, "", new ArrayList<>());
        Book updateBook = new Book(id, requestBook.getTitle(), requestBook.getYear(), new Person(), new ArrayList<>());

        when(bookMapper.toEntity(requestBook)).thenReturn(updateBook);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        BookResponseDTO updateById = booksService.updateById(id, requestBook);

        verify(bookMapper, times(1)).toEntity(requestBook);
        verify(bookRepository, times(1)).findById(id);
        Assertions.assertNull(updateById);
    }

    @Test
    void deleteTrueTest() {
        UUID id = UUID.randomUUID();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        boolean delete = booksService.delete(id);

        Assertions.assertTrue(delete);
    }

    @Test
    void deleteFalseTest() {
        UUID id = UUID.randomUUID();
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        boolean delete = booksService.delete(id);

        Assertions.assertFalse(delete);
    }
}