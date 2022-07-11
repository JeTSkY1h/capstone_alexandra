package com.github.JeTSkY1h.book;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Books")
public class Book {
    @Id
    String id;
    String title;
    String author;
    String filePath;
    List<String> genre;
    String coverPath;

}
