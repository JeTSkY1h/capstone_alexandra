package com.github.JeTSkY1h;

import com.github.JeTSkY1h.book.Book;
import com.github.JeTSkY1h.book.BookRepo;
import com.github.JeTSkY1h.book.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class BookServiceTest {

    BookRepo bookRepo = Mockito.mock(BookRepo.class);
    String bookpath = BookServiceTest.class.getResource("books").getPath();
    BookService bookService = new BookService(bookRepo, bookpath);

    @Test
    void shouldAddBooks() throws Exception{
        bookService.refresh();
        Mockito.verify(bookRepo).saveAll(List.of(new Book("Harry Potter - Gesamtausgabe", "Rowling, Joanne K.", "/Users/jetsky/IdeaProjects/capstone_alexandra/backend/target/test-classes/com/github/JeTSkY1h/books/harrypotter.epub", List.of("Fantasy", "Magie", "Jugendbuch"), "/Users/jetsky/IdeaProjects/capstone_alexandra/backend/target/test-classes/com/github/JeTSkY1h/books/HarryPotter-Gesamtausgabe.png")));
    }

    @Test
    void shouldListBooks(){
        bookService.getBooks();
        Mockito.verify(bookRepo).findAll();
    }

    @Test
    void shouldFindBookById(){
        bookService.getById("testId123abc");
        Mockito.verify(bookRepo.findById("testId123abc"));
    }
}
