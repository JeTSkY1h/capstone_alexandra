package com.github.JeTSkY1h.book;


import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    EpubReader epubReader = new EpubReader();

    @GetMapping("/refresh")
    void refreshBooklist(){
        bookService.refresh();
    }
    @GetMapping("/{id}")
    ResponseEntity<com.github.JeTSkY1h.book.Book>  getBook(@PathVariable String id) {
        return ResponseEntity.of( bookService.getById(id) );
    }

    @GetMapping()
    ResponseEntity<List<Book>> test(){
        return ResponseEntity.ok(bookService.getBooks());
    }
}
