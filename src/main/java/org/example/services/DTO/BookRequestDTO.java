package org.example.services.DTO;

import java.util.List;

public class BookRequestDTO {
    private String title;
    private int year;
    private String ownerId;
    private List<String> authorsId;

    public BookRequestDTO() {
    }

    public BookRequestDTO(String title, int year, String ownerId, List<String> authorsId) {
        this.title = title;
        this.year = year;
        this.ownerId = ownerId;
        this.authorsId = authorsId;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<String> getAuthorsId() {
        return authorsId;
    }
}
