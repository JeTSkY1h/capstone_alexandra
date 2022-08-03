package com.github.JeTSkY1h.book;


import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/search/{query}")
    ResponseEntity<List<Book>> searchBookBytitle(@PathVariable String query){
        return ResponseEntity.ok(bookService.searchByTitle(query));
    }

    @PutMapping("/{id}/rate")
    ResponseEntity<Book> rateBook(@PathVariable String id, @RequestBody RatingResponse ratingResponse) {
        return ResponseEntity.of(Optional.of(bookService.rateBook(id, ratingResponse.getRating())));
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
            value="/{id}/images/{resHref}",
            produces= MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getResourceImgImg(@PathVariable String id, @PathVariable String resHref ) throws Exception{
        return bookService.getResource(id, resHref);
    }

    @GetMapping(
            value="/{id}/css/{resHref}",
            produces = "text/css"
    )
    public @ResponseBody byte[] getResourceCSS(@PathVariable String id, @PathVariable String resHref) throws Exception {
        return bookService.getResource(id, resHref);
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
