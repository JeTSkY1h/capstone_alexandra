package com.example.demo.book;

import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    EpubReader epubReader = new EpubReader();
    Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }



}
