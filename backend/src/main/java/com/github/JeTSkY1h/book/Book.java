package com.github.JeTSkY1h.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.util.List;


@Data
@Document(collection = "Books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private String description;
    private URL filePath;
    private List<String> genre;
    private String coverPath;
    private Integer rating;
    private Integer rated;


    public Book(String title, String author, URL filePath, List<String> genre, String coverPath) {
        this.title = title;
        this.author = author;
        this.filePath = filePath;
        this.genre = genre;
        this.coverPath = coverPath;
    }




}
