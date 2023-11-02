package org.example.services.DTO;

public class AuthorRequestDTO {

    private String fullName;

    public AuthorRequestDTO() {
    }

    public AuthorRequestDTO(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
