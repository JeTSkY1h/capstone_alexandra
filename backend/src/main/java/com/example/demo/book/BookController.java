package com.example.demo.book;


import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
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



    @GetMapping("/{id}")
    ResponseEntity<Book>  getBook(@PathVariable String id) {
        return ResponseEntity.of( bookService.getById(id) );
    }

    @GetMapping()
    ResponseEntity<String> test(){
        try {
            Book book = epubReader.readEpub(new FileInputStream("backend/harrypotter.epub"));
            SpineReference spineReference = book.getSpine().getSpineReferences().get(45);
            String xhtml = new String(spineReference.getResource().getData());
            String text = Jsoup.parse(xhtml).text();
            return ResponseEntity.ok(xhtml);
        } catch(Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
