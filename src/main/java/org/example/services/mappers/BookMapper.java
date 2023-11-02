package org.example.services.mappers;

import org.example.entities.Book;
import org.example.services.DTO.BookRequestDTO;
import org.example.services.DTO.BookResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface BookMapper {

    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "year", target = "year"),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "authors", ignore = true)
    })
    Book toEntity(BookRequestDTO bookRequestDTO);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "owner", target = "owner", qualifiedByName = "toPersonResponseDto")
    })
    BookResponseDTO toResponseDTO(Book book);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "owner", target = "owner", qualifiedByName = "toPersonResponseDto")
    })
    @Named("toBookDTOList")
    List<BookResponseDTO> toDTOList(List<Book> books);
}
