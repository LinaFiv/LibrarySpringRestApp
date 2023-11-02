package org.example.services;

import org.example.entities.Author;
import org.example.entities.Book;
import org.example.entities.Person;
import org.example.repositories.BooksRepository;
import org.example.services.DTO.BookRequestDTO;
import org.example.services.DTO.BookResponseDTO;
import org.example.services.mappers.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final BookMapper bookMapper;
    private final AuthorsService authorsService;

    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, BookMapper bookMapper,
                        AuthorsService authorsService, @Lazy PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.bookMapper = bookMapper;
        this.authorsService = authorsService;
        this.peopleService = peopleService;
    }

    public BookResponseDTO findById(UUID id) {
        Book book = booksRepository.findById(id).orElse(null);
        return bookMapper.toResponseDTO(book);
    }

    public List<BookResponseDTO> findAll() {
        List<Book> book = booksRepository.findAll();
        return bookMapper.toDTOList(book);
    }

    public List<Book> findAllById(List<String> ids) {
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        return booksRepository.findAllById(uuids);
    }

    public BookResponseDTO create(BookRequestDTO book) {
        List<Author> authors = authorsService.findAllById(book.getAuthorsId());
        Person person = peopleService.findById(book.getOwnerId());

        Book bookEntity = bookMapper.toEntity(book);
        bookEntity.setOwner(person);
        bookEntity.setAuthors(authors);
        Book createBook = booksRepository.save(bookEntity);
        return bookMapper.toResponseDTO(createBook);
    }

    public void updatePersonId(List<Book> books, Person person) {
        books.forEach(book -> book.setOwner(person));
        booksRepository.saveAll(books);
    }

    public void updateNullPersonId(List<Book> books) {
        books.forEach(book -> book.setOwner(null));
        booksRepository.saveAll(books);
    }

    public BookResponseDTO updateById(UUID id, BookRequestDTO updateBook) {
        Book updateBookEntity = bookMapper.toEntity(updateBook);
        Person person = peopleService.findById(updateBook.getOwnerId());
        List<Author> authors = authorsService.findAllById(updateBook.getAuthorsId());
        Book book = booksRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(updateBookEntity.getTitle());
            book.setYear(updateBookEntity.getYear());
            book.setOwner(person);
            book.setAuthors(authors);
            return bookMapper.toResponseDTO(booksRepository.save(book));
        }
        return null;
    }

    public boolean delete(UUID id) {
        booksRepository.deleteById(id);
        return booksRepository.findById(id).isEmpty();
    }
}
