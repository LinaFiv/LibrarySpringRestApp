package org.example.services.mappers;

import org.example.entities.Author;
import org.example.services.DTO.AuthorRequestDTO;
import org.example.services.DTO.AuthorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mappings({
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(target = "books", ignore = true)
    })
    Author toEntity(AuthorRequestDTO authorRequestDTO);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "fullName", target = "fullName")
    })
    AuthorResponseDTO toResponseDTO(Author author);

    @Named("toAuthorDTOList")
    List<AuthorResponseDTO> toDTOList(List<Author> authors);
}
