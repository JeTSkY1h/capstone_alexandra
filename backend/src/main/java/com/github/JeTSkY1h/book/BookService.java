package com.github.JeTSkY1h.book;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import nl.siegmann.epublib.util.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookService {

    private final BookRepo bookRepo;
    EpubReader epubReader = new EpubReader();
    @Value("${cloudinary.api.key}")
    private String key;
    @Value("${cloudinary.api.secret}")
    private String secret;
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }
    public Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }
    public List<Book> getBooks() {
        return bookRepo.findAll();
    }

    public List<Book> refresh() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djxtc8lav",
                "api_key", key,
                "api_secret", secret));
        ArrayList<HashMap> cloudBooks;
        List<URL> bookURLs;
        List<Book> res = new ArrayList<>();
        try {
            cloudBooks = (ArrayList<HashMap>) cloudinary.api().resources(ObjectUtils.asMap("resource_type", "raw")).get("resources");
            bookURLs = cloudBooks.stream().map(cloudBook-> {
                try {
                    return new URL((String) cloudBook.get("secure_url"));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

            bookURLs.forEach(bookFile -> {
                System.out.println("Test"+  bookFile.getPath());
                try (InputStream fIn = bookFile.openStream()) {
                    nl.siegmann.epublib.domain.Book book = epubReader.readEpub(fIn);
                    Book bookRes = new Book();
                    Map imageUploadRes = cloudinary.uploader().upload(book.getCoverImage().getData(), ObjectUtils.emptyMap());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public byte[] getCoverByID(String id) throws IOException {
        Book book = getById(id).orElseThrow();
        InputStream in = new BufferedInputStream(new FileInputStream(book.getCoverPath()));
        return IOUtil.toByteArray(in);
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
