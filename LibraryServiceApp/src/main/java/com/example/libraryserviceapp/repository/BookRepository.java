package com.example.libraryserviceapp.repository;

import com.example.libraryserviceapp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book,UUID> {
    int countByUserId(UUID userId);
}
