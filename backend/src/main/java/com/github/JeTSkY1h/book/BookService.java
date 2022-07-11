package com.github.JeTSkY1h.book;

import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    EpubReader epubReader = new EpubReader();
    Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }

    List<Book> getBooks(){
        return bookRepo.findAll();
    }

    public void refresh() {
        List<File> books = new ArrayList<>();
        File f = new File("books/");
        books = Arrays.asList(f.listFiles());
        for(File bookFile : books){
            try {
                nl.siegmann.epublib.domain.Book book = epubReader.readEpub(new FileInputStream(bookFile.getPath()));
                Book bookRes = new Book();
                BufferedImage buffCoverImg = ImageIO.read(book.getCoverImage().getInputStream());
                File outputFile = new File(book.getTitle().replace(" ","")+".png");
                ImageIO.write(buffCoverImg, "png", outputFile);
                bookRes.setCoverPath(outputFile.getAbsolutePath());
                bookRes.setAuthor(book.getMetadata().getAuthors().get(0).toString());
                bookRes.setFilePath(bookFile.getPath());
                bookRes.setGenre(book.getMetadata().getSubjects());
                bookRes.setTitle(book.getTitle());
                bookRepo.save(bookRes);
            } catch(Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
