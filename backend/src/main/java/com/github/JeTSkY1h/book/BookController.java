package com.github.JeTSkY1h.book;


import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@EnableWebMvc
public class BookController {
    private final BookService bookService;

    @PutMapping("/{id}/rate")
    ResponseEntity<Book> rateBook(@PathVariable String id, @RequestBody Integer newRating) throws Exception {
        return ResponseEntity.of(Optional.of(bookService.rateBook(id, newRating)));
    }

    @GetMapping(("/{id}/epub"))
    public @ResponseBody byte[] getEpubbook(@PathVariable String id) throws Exception{
       return bookService.getEpub(id);
    }

    @GetMapping("/refresh")
    void refreshBooklist(){
        bookService.refresh();
    }

    @GetMapping("/{id}/chapter")
    ResponseEntity<List<String>> getChapters(@PathVariable String id) throws Exception{
        return ResponseEntity.ok(bookService.getChapters(id));
    }

    @GetMapping("/{id}/chapter/{chapter}")
    String getChapter(@PathVariable String id, @PathVariable int chapter){
        return bookService.getChapter(id,chapter);
    }

    @GetMapping(
            value="/cover/{id}",
            produces= MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getCoverImg(@PathVariable String id) throws IOException {
        return bookService.getCoverByID(id);
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
