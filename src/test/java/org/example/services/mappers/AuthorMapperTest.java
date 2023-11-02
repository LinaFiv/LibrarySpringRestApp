package org.example.services.mappers;

import org.example.entities.Author;
import org.example.services.DTO.AuthorRequestDTO;
import org.example.services.DTO.AuthorResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class AuthorMapperTest {

    AuthorMapper authorMapper = new AuthorMapperImpl();

    Author author;

    AuthorRequestDTO requestDTO;

    AuthorResponseDTO responseDTO;

    @AfterEach
    void tearDown() {
        author = null;
        requestDTO = null;
        responseDTO = null;
    }

    @Test
    void toEntityTest() {
        author = new Author("Author", null);
        requestDTO = new AuthorRequestDTO(author.getFullName());
        Author actualEntity = authorMapper.toEntity(requestDTO);

        Assertions.assertEquals(author, actualEntity);
    }

    @Test
    void toResponseDTOTest() {
        author = new Author(UUID.randomUUID(), "Author");
        responseDTO = new AuthorResponseDTO(author.getId(), author.getFullName());
        AuthorResponseDTO actualDTO = authorMapper.toResponseDTO(author);

        Assertions.assertEquals(responseDTO, actualDTO);
    }

    @Test
    void toDTOListTest() {
        List<Author> authors = getAuthors();
        List<AuthorResponseDTO> responseList = getResponseList();
        List<AuthorResponseDTO> actualDTOList = authorMapper.toDTOList(authors);

        Assertions.assertEquals(responseList, actualDTOList);
    }

    private List<AuthorResponseDTO> getResponseList() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        String id3 = "523fa7f6-1046-49ef-ab02-0a66e40ced28";

        return new ArrayList<>(List.of(
                new AuthorResponseDTO(UUID.fromString(id1), "Author1"),
                new AuthorResponseDTO(UUID.fromString(id2), "Author2"),
                new AuthorResponseDTO(UUID.fromString(id3), "Author3")
        ));
    }

    private List<Author> getAuthors() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        String id3 = "523fa7f6-1046-49ef-ab02-0a66e40ced28";

        return new ArrayList<>(List.of(
                new Author(UUID.fromString(id1), "Author1"),
                new Author(UUID.fromString(id2), "Author2"),
                new Author(UUID.fromString(id3), "Author3")
        ));
    }
}