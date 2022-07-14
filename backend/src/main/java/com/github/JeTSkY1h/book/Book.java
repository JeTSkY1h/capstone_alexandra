package com.github.JeTSkY1h.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    String id;
    String title;
    String author;
    String filePath;
    List<String> genre;
    String coverPath;

    Integer rating;

    Integer rated;


    public Book(String title, String author, String filePath, List<String> genre, String coverPath) {
        this.title = title;
        this.author = author;
        this.filePath = filePath;
        this.genre = genre;
        this.coverPath = coverPath;
    }




}
