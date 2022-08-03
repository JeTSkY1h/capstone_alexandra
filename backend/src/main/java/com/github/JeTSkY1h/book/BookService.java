package com.github.JeTSkY1h.book;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import nl.siegmann.epublib.util.IOUtil;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    private final CloudinaryService cloudinaryService;
    EpubReader epubReader = new EpubReader();
    public Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }
    public List<Book> getBooks() {
        return bookRepo.findAll();
    }

    public List<Book> refresh() {

        List<URL> bookURLs = cloudinaryService.getBookURLs();

        List<Book> res = new ArrayList<>();

            bookURLs.forEach(bookFile -> {
                System.out.println("Test"+  bookFile.getPath());
                try (InputStream fIn = bookFile.openStream()) {
                    nl.siegmann.epublib.domain.Book book = epubReader.readEpub(fIn);
                    Book bookRes = new Book();
                    Map imageUploadRes = cloudinaryService.uploadCover(book.getCoverImage().getData());
                    bookRes.setCoverPath((String) imageUploadRes.get("secure_url"));
                    bookRes.setAuthor(book.getMetadata().getAuthors().get(0).toString());
                    bookRes.setFilePath(bookFile);
                    bookRes.setGenre(book.getMetadata().getSubjects());
                    bookRes.setTitle(book.getTitle());
                    System.out.println(book.getMetadata().getDescriptions());
                    if(book.getMetadata().getDescriptions().size() > 0) {
                        bookRes.setDescription(book.getMetadata().getDescriptions().get(0));
                    }
                    res.add(bookRes);
                    bookRepo.saveAll(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        return res;
    }

    public byte[] getCoverByID(String id) throws IOException {
        Book book = getById(id).orElseThrow();
        return book.getCoverPath().getBytes();
    }

    public List<String> getChapters(String id) throws Exception {

        Book book = getById(id).orElseThrow();
        try (InputStream fIn = book.getFilePath().openStream()) {
            nl.siegmann.epublib.domain.Book epubBook = epubReader.readEpub(fIn);
            List<TOCReference> tocReferences = epubBook.getTableOfContents().getTocReferences();
            List<SpineReference> spineReferences = epubBook.getSpine().getSpineReferences();
            System.out.println(epubBook.getResources().getResourcesByMediaType(MediatypeService.CSS));
            List<String> chapters = new ArrayList<>();
            if (tocReferences.size() < spineReferences.size()) {
                for (int i = 0; i < spineReferences.size(); i++) {
                    SpineReference spineReference = spineReferences.get(i);
                    String res = spineReference.getResource().getTitle() == null ? "Kapitel: " + i : spineReference.getResource().getTitle();
                    chapters.add(res);
                }
            } else {
                for (int i = 0; i < tocReferences.size(); i++) {
                    TOCReference resource = tocReferences.get(i);
                    String res = resource.getTitle() == null ? "kapitel" + i : resource.getTitle();
                    chapters.add(res);
                }
            }
            return chapters;
        }
    }

    public String getChapter(String id, int chapter) {
        Book book = getById(id).orElseThrow();
        try (InputStream fIn = book.getFilePath().openStream()){
            nl.siegmann.epublib.domain.Book ebupBook = epubReader.readEpub(fIn);
            List<SpineReference> references = ebupBook.getSpine().getSpineReferences();
            String xhtmlString = new String(references.get(chapter).getResource().getData());
            Pattern p = Pattern.compile("<img src=\"(.*?\")(.*)/>");
            Matcher m = p.matcher(xhtmlString);
            String res = xhtmlString;
            if (m.find()) {
                res = m.replaceAll("<img src=\"" + "/api/books/" + id + "/images/$1$2" + "/>");
            }
            res = res
                    .replaceAll("<a.*href=.*>", "<p>")
                    .replaceAll("</a>", "</p>");



            return res;
        } catch (Exception e) {
            return "Es gab einen Fehler Beim Laden des Kapitels.";
        }
    }

    //get average Rating
    public Book rateBook(String id, Integer rating) {
        Book book = getById(id).orElseThrow();
        Integer rated = book.getRated();
        if(rated == null) rated = 0;
        Integer currRating = book.getRating();
        if(currRating == null) currRating = 0;
        rated++;
        book.setRated(rated);
        double newRating = (rating - currRating) / rated;
        Integer res = (int)newRating + currRating;
        book.setRating(res);
        bookRepo.save(book);
        return book;
    }

    public byte[] getResource(String id, String resHref) throws Exception {
        Book book = getById(id).orElseThrow();
        try (InputStream fIn = book.getFilePath().openStream()) {
            nl.siegmann.epublib.domain.Book ebupBook = epubReader.readEpub(fIn);
            return ebupBook.getResources().getByHref(resHref).getData();
        }
    }

    public List<Book> searchByTitle(String query) {
        return bookRepo.findBooksByTitleContainingIgnoreCase(query);
    }
}
