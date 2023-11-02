package org.example.services.mappers;

import org.example.entities.Person;
import org.example.services.DTO.PersonRequestDTO;
import org.example.services.DTO.PersonResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class PersonMapperTest {

    PersonMapper personMapper = new PersonMapperImpl();

    Person person;

    PersonRequestDTO requestDTO;

    PersonResponseDTO responseDTO;

    @AfterEach
    void tearDown() {
        person = null;
        requestDTO = null;
        responseDTO = null;
    }

    @Test
    void toEntityTest() {
        person = new Person("Test", 1990);
        requestDTO = new PersonRequestDTO(person.getFullName(), person.getYearOfBirth(), new ArrayList<>());
        Person actualEntity = personMapper.toEntity(requestDTO);

        Assertions.assertEquals(person, actualEntity);
    }

    @Test
    void toResponseDTOTest() {
        person = new Person(UUID.randomUUID(), "Test", 1990);
        responseDTO = new PersonResponseDTO(person.getId(), person.getFullName(), person.getYearOfBirth());
        PersonResponseDTO actualDTO = personMapper.toResponseDTO(person);

        Assertions.assertEquals(responseDTO, actualDTO);
    }

    @Test
    void toDTOListTest() {
        List<Person> people = getPeople();
        List<PersonResponseDTO> responseList = getResponseList();
        List<PersonResponseDTO> actualDTOList = personMapper.toDTOList(people);

        Assertions.assertEquals(responseList, actualDTOList);
    }

    private List<PersonResponseDTO> getResponseList() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        String id3 = "523fa7f6-1046-49ef-ab02-0a66e40ced28";

        return new ArrayList<>(List.of(
                new PersonResponseDTO(UUID.fromString(id1), "Test1", 1990),
                new PersonResponseDTO(UUID.fromString(id2), "Test2", 1990),
                new PersonResponseDTO(UUID.fromString(id3), "Test3", 1990)
        ));
    }

    private List<Person> getPeople() {
        String id1 = "108a3313-60a4-4bab-86e8-66c0359076f5";
        String id2 = "52563dcc-2462-4f19-80f4-4b6354a9680a";
        String id3 = "523fa7f6-1046-49ef-ab02-0a66e40ced28";

        return new ArrayList<>(List.of(
                new Person(UUID.fromString(id1), "Test1", 1990),
                new Person(UUID.fromString(id2), "Test2", 1990),
                new Person(UUID.fromString(id3), "Test3", 1990)
        ));
    }
}