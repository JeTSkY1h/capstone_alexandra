package com.github.JeTSkY1h.book;

import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.util.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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

    public byte[] getEpub(String id) throws Exception{
        Book book =  getById(id).orElseThrow();
        InputStream in = getClass()
                .getResourceAsStream(book.getFilePath());
        return IOUtil.toByteArray(in);

    }

    public Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }

    public List<Book> getBooks(){
        return bookRepo.findAll();
    }

    public List<Book> refresh(){
        List<File> books;
        File f = new File(path);
        books = Arrays.stream(f.listFiles()).filter(bookFile->bookFile.getAbsolutePath().endsWith(".epub")).toList();
        List<Book> res = new ArrayList<>();
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
        bookRepo.saveAll(res);
        return res;
    }

    public byte[] getCoverByID(String id) throws IOException {
        Book book = getById(id).orElseThrow();
        System.out.println(book);
        InputStream in = new BufferedInputStream(new FileInputStream(book.getCoverPath()));
        return IOUtil.toByteArray(in);
    }

    public List<String> getChapters(String id) throws Exception{
        Book book = getById(id).orElseThrow();
        try (FileInputStream fIn = new FileInputStream(book.getFilePath())) {
            nl.siegmann.epublib.domain.Book epubBook = epubReader.readEpub(fIn);
            List<TOCReference> resources = epubBook.getTableOfContents().getTocReferences();
            List<String> chapters = new ArrayList<>();
            for (int i = 0; i < resources.size(); i++) {
                TOCReference resource = resources.get(i);
                String res = resource.getTitle() == null ? "kapitel" + i : resource.getTitle();
                chapters.add(res);
            }
            return chapters;
        }
    }

    public String getChapter(String id, int chapter) {
        Book book = getById(id).orElseThrow();
        try(FileInputStream fIn = new FileInputStream(book.getFilePath())) {
            nl.siegmann.epublib.domain.Book ebupBook = epubReader.readEpub(fIn);
            List< SpineReference> refrences = ebupBook.getSpine().getSpineReferences();
            return  new String(refrences.get(chapter).getResource().getData());
        } catch (Exception e) {
            return "Es gab einen Fehler Beim Laden des Kapitels.";
        }
    }

    //get average Rating
    public Book rateBook(String id, Integer rating) throws Exception {
        Book book = getById(id).orElseThrow();
        Integer rated = book.getRated();
        Integer currRating = book.getRating();
        book.setRated(rated+1);
        Integer newRating = (rating - currRating)/rated;
        book.setRating(newRating +currRating);
        return bookRepo.save(book);
    }
}
