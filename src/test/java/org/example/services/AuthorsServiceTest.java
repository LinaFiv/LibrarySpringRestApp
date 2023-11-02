package org.example.services;

import org.example.entities.Author;
import org.example.repositories.AuthorsRepository;
import org.example.services.DTO.AuthorRequestDTO;
import org.example.services.DTO.AuthorResponseDTO;
import org.example.services.mappers.AuthorMapper;
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
class AuthorsServiceTest {

    @Mock
    AuthorsRepository authorsRepository;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    AuthorsService authorsService;

    @BeforeEach
    void setUp() {
        authorsService = new AuthorsService(authorsRepository, authorMapper);
    }

    @Test
    void findByIdTest() {
        Author author = new Author(UUID.randomUUID(), "Author", new ArrayList<>());
        AuthorResponseDTO responseDTO = new AuthorResponseDTO(UUID.randomUUID(), "Author");
        when(authorsRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(authorMapper.toResponseDTO(author)).thenReturn(responseDTO);
        AuthorResponseDTO byId = authorsService.findById(author.getId());

        verify(authorsRepository, times(1)).findById(author.getId());
        verify(authorMapper, times(1)).toResponseDTO(author);
        Assertions.assertEquals(byId, responseDTO);
    }

    @Test
    void findAllTest() {
        List<Author> authors = new ArrayList<>(List.of(
                new Author(UUID.randomUUID(), "Author", new ArrayList<>()),
                new Author(UUID.randomUUID(), "Author", new ArrayList<>()),
                new Author(UUID.randomUUID(), "Author", new ArrayList<>())
        ));
        ArrayList<AuthorResponseDTO> dtos = new ArrayList<>(List.of(
                new AuthorResponseDTO(UUID.randomUUID(), "Author1"),
                new AuthorResponseDTO(UUID.randomUUID(), "Author2"),
                new AuthorResponseDTO(UUID.randomUUID(), "Author3")
        ));

        when(authorsRepository.findAll()).thenReturn(authors);
        when(authorMapper.toDTOList(authors)).thenReturn(dtos);
        List<AuthorResponseDTO> all = authorsService.findAll();

        verify(authorsRepository, times(1)).findAll();
        verify(authorMapper, times(1)).toDTOList(authors);
        Assertions.assertEquals(all, dtos);
    }

    @Test
    void findAllById() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        List<String> ids = new ArrayList<>(List.of(id1, id2));
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        List<Author> authors = new ArrayList<>(List.of(
                new Author(UUID.fromString(id1), "Author1"),
                new Author(UUID.fromString(id2), "Author2")
        ));
        when(authorsRepository.findAllById(uuids)).thenReturn(authors);
        List<Author> all = authorsService.findAllById(ids);

        verify(authorsRepository, times(1)).findAllById(uuids);
        Assertions.assertEquals(authors, all);
    }

    @Test
    void createTest() {
        AuthorRequestDTO requestAuthor = new AuthorRequestDTO("AuthorCreate");
        Author author = new Author(UUID.randomUUID(), requestAuthor.getFullName(), new ArrayList<>());
        AuthorResponseDTO responseAuthor = new AuthorResponseDTO(author.getId(), author.getFullName());

        when(authorMapper.toEntity(requestAuthor)).thenReturn(author);
        when(authorsRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponseDTO(author)).thenReturn(responseAuthor);
        AuthorResponseDTO create = authorsService.create(requestAuthor);

        verify(authorMapper, times(1)).toEntity(requestAuthor);
        verify(authorsRepository, times(1)).save(author);
        verify(authorMapper, times(1)).toResponseDTO(author);
        Assertions.assertEquals(create, responseAuthor);
    }

    @Test
    void updateByFindIdTest() {
        UUID id = UUID.randomUUID();
        Author author = new Author(UUID.randomUUID(), "Author", new ArrayList<>());
        AuthorRequestDTO requestAuthor = new AuthorRequestDTO("UpdateName");
        Author updateAuthor = new Author(id, requestAuthor.getFullName(), new ArrayList<>());
        AuthorResponseDTO responseAuthor = new AuthorResponseDTO(id, updateAuthor.getFullName());

        when(authorMapper.toEntity(requestAuthor)).thenReturn(updateAuthor);
        when(authorsRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorMapper.toResponseDTO(authorsRepository.save(author))).thenReturn(responseAuthor);
        AuthorResponseDTO updateById = authorsService.updateById(id, requestAuthor);

        verify(authorMapper, times(1)).toEntity(requestAuthor);
        verify(authorsRepository, times(1)).findById(id);
        verify(authorMapper, times(1)).toResponseDTO(authorsRepository.save(author));
        Assertions.assertEquals(updateById, responseAuthor);
    }

    @Test
    void updateByNotFindIdTest() {
        UUID id = UUID.randomUUID();
        AuthorRequestDTO requestAuthor = new AuthorRequestDTO("UpdateName");
        Author updateAuthor = new Author(id, requestAuthor.getFullName(), new ArrayList<>());

        when(authorMapper.toEntity(requestAuthor)).thenReturn(updateAuthor);
        when(authorsRepository.findById(id)).thenReturn(Optional.empty());
        AuthorResponseDTO updateById = authorsService.updateById(id, requestAuthor);

        verify(authorMapper, times(1)).toEntity(requestAuthor);
        verify(authorsRepository, times(1)).findById(id);
        Assertions.assertNull(updateById);
    }

    @Test
    void deleteTrueTest() {
        UUID id = UUID.randomUUID();
        when(authorsRepository.findById(id)).thenReturn(Optional.empty());
        boolean delete = authorsService.delete(id);

        Assertions.assertTrue(delete);
    }

    @Test
    void deleteFalseTest() {
        UUID id = UUID.randomUUID();
        Author author = new Author();
        when(authorsRepository.findById(id)).thenReturn(Optional.of(author));
        boolean delete = authorsService.delete(id);

        Assertions.assertFalse(delete);
    }
}