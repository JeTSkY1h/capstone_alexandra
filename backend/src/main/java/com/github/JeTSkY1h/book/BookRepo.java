package com.github.JeTSkY1h.book;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends MongoRepository<Book, String> {
    List<Book> findBooksByTitleContainingIgnoreCase(String title);
}
