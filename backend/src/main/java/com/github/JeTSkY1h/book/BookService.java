package com.github.JeTSkY1h.book;

import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
@Service
public class BookService {


    private final BookRepo bookRepo;
    private final String path;
    EpubReader epubReader = new EpubReader();

    public BookService(BookRepo bookRepo, @Value("${app.book.path}") String path){
        this.bookRepo = bookRepo;
        this.path = path;
    }

    public Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }

    public List<Book> getBooks(){
        return bookRepo.findAll();
    }

    public List<Book> refresh() {
        List<File> books;
        File f = new File(path);
        books = Arrays.stream(f.listFiles()).filter(bookFile->bookFile.getAbsolutePath().endsWith(".epub")).toList();
        List<Book> res = new ArrayList<>();
       if(books != null) {
           for (File bookFile : books) {
               try (FileInputStream fIn = new FileInputStream(bookFile.getPath())) {
                   nl.siegmann.epublib.domain.Book book = epubReader.readEpub(fIn);
                   Book bookRes = new Book();
                   BufferedImage buffCoverImg = ImageIO.read(book.getCoverImage().getInputStream());
                   File outputFile = new File(path +"/"+ book.getTitle().replaceAll("[-+/\\.:'*;,]", "") + ".png");
                   ImageIO.write(buffCoverImg, "png", outputFile);
                   bookRes.setCoverPath(outputFile.getAbsolutePath());
                   bookRes.setAuthor(book.getMetadata().getAuthors().get(0).toString());
                   bookRes.setFilePath(bookFile.getPath());
                   bookRes.setGenre(book.getMetadata().getSubjects());
                   bookRes.setTitle(book.getTitle());
                   res.add(bookRes);
               } catch (Exception e) {
                   System.err.println(e.getMessage());
               }
           }
       }
        bookRepo.saveAll(res);
        return res;
    }
}
