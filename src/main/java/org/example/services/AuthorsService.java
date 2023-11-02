package org.example.services;

import org.example.entities.Author;
import org.example.repositories.AuthorsRepository;
import org.example.services.DTO.AuthorRequestDTO;
import org.example.services.DTO.AuthorResponseDTO;
import org.example.services.mappers.AuthorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorsService {

    private final AuthorsRepository authorsRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorsService(AuthorsRepository authorsRepository, AuthorMapper authorMapper) {
        this.authorsRepository = authorsRepository;
        this.authorMapper = authorMapper;
    }

    public AuthorResponseDTO findById(UUID id) {
        Author author = authorsRepository.findById(id).orElse(null);
        return authorMapper.toResponseDTO(author);
    }

    public List<AuthorResponseDTO> findAll() {
        List<Author> author = authorsRepository.findAll();
        return authorMapper.toDTOList(author);
    }

    public List<Author> findAllById(List<String> ids) {
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        return authorsRepository.findAllById(uuids);
    }

    public AuthorResponseDTO create(AuthorRequestDTO author) {
        Author authorEntity = authorMapper.toEntity(author);
        Author createAuthor = authorsRepository.save(authorEntity);
        return authorMapper.toResponseDTO(createAuthor);
    }

    public AuthorResponseDTO updateById(UUID id, AuthorRequestDTO updateAuthor) {
        Author updateAuthorEntity = authorMapper.toEntity(updateAuthor);
        Author author = authorsRepository.findById(id).orElse(null);
        if (author != null) {
            author.setFullName(updateAuthorEntity.getFullName());
            author.setBooks(updateAuthorEntity.getBooks());
            return authorMapper.toResponseDTO(authorsRepository.save(author));
        }
        return null;
    }

    public boolean delete(UUID id) {
        authorsRepository.deleteById(id);
        return authorsRepository.findById(id).isEmpty();
    }
}
