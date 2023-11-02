package org.example.services.mappers;

import org.example.entities.Book;
import org.example.entities.Person;
import org.example.services.DTO.BookRequestDTO;
import org.example.services.DTO.BookResponseDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @InjectMocks
    BookMapper bookMapper = new BookMapperImpl();

    @Mock
    PersonMapper personMapper;

    Book book;

    Person owner;

    BookRequestDTO requestDTO;

    BookResponseDTO responseDTO;

    @AfterEach
    void tearDown() {
        owner = null;
        book = null;
        requestDTO = null;
        responseDTO = null;
    }

    @Test
    void toEntityTest() {
        book = new Book("Title", 1900);
        requestDTO = new BookRequestDTO(book.getTitle(), book.getYear(), null, new ArrayList<>());
        Book actualEntity = bookMapper.toEntity(requestDTO);

        Assertions.assertEquals(book, actualEntity);
    }

    @Test
    void toResponseDTOTest() {
        owner = new Person(UUID.randomUUID(), "Test", 1990);
        book = new Book(UUID.randomUUID(), "Title", 1900);
        book.setOwner(owner);
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(owner.getId(), owner.getFullName(), owner.getYearOfBirth());
        responseDTO = new BookResponseDTO(book.getId(), book.getTitle(), book.getYear(), personResponseDTO);
        when(personMapper.toResponseDTO(owner)).thenReturn(personResponseDTO);
        BookResponseDTO actualDTO = bookMapper.toResponseDTO(book);

        Assertions.assertEquals(responseDTO, actualDTO);
    }

    @Test
    void toDTOListTest() {
        owner = new Person();
        PersonResponseDTO ownerDTO = new PersonResponseDTO();
        List<Book> books = getBooks();
        List<BookResponseDTO> responseList = getResponseList();
        when(personMapper.toResponseDTO(owner)).thenReturn(ownerDTO);
        List<BookResponseDTO> actualDTOList = bookMapper.toDTOList(books);

        Assertions.assertEquals(responseList, actualDTOList);
    }

    private List<BookResponseDTO> getResponseList() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";

        return new ArrayList<>(List.of(
                new BookResponseDTO(UUID.fromString(id1), "Title1", 1900, new PersonResponseDTO(null, null, 0)),
                new BookResponseDTO(UUID.fromString(id2), "Title2", 1900, new PersonResponseDTO(null, null, 0))
        ));
    }

    private List<Book> getBooks() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";

        return new ArrayList<>(List.of(
                new Book(UUID.fromString(id1), "Title1", 1900, owner),
                new Book(UUID.fromString(id2), "Title2", 1900, owner)
        ));
    }
}