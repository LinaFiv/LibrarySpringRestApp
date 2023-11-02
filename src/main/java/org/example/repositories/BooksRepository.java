package org.example.repositories;

import org.example.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Book, UUID> {
}
