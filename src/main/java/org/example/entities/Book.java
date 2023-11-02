package org.example.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private int year;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;
    @ManyToMany(mappedBy = "books")
    private List<Author> authors;

    public Book() {
    }

    public Book(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public Book(UUID id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public Book(UUID id, String title, int year, Person owner) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.owner = owner;
    }

    public Book(UUID id, String title, int year, Person owner, List<Author> authors) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.owner = owner;
        this.authors = authors;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(owner, book.owner) && Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, year, owner, authors);
    }
}
