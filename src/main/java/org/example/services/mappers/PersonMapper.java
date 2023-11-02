package org.example.services.mappers;

import org.example.entities.Person;
import org.example.services.DTO.PersonRequestDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mappings({
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(source = "yearOfBirth", target = "yearOfBirth"),
            @Mapping(target = "books", ignore = true)
    })
    @Named("toPersonEntity")
    Person toEntity(PersonRequestDTO personRequestDTO);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(source = "yearOfBirth", target = "yearOfBirth")
    })
    @Named("toPersonResponseDto")
    PersonResponseDTO toResponseDTO(Person person);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(source = "yearOfBirth", target = "yearOfBirth")
    })
    List<PersonResponseDTO> toDTOList(List<Person> people);
}
