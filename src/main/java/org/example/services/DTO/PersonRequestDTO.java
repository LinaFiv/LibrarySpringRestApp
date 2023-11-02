package org.example.services.DTO;

import java.util.List;

public class PersonRequestDTO {

    private String fullName;
    private int yearOfBirth;
    private List<String> booksId;

    public PersonRequestDTO() {
    }

    public PersonRequestDTO(String fullName, int yearOfBirth, List<String> booksId) {
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
        this.booksId = booksId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public List<String> getBooksId() {
        return booksId;
    }
}
