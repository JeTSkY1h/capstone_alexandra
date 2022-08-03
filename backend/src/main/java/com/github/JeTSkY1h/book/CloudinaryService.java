package com.github.JeTSkY1h.book;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CloudinaryService {


    private final Cloudinary cloudinary;

    public Map<String, Object> uploadCover(byte[] coverPic) {

        try {
            return  cloudinary.uploader().upload(coverPic, ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
            return  Map.of();
        }

    }


    public List<URL> getBookURLs() {
        ArrayList<HashMap> cloudBooks;
        try {
            cloudBooks = (ArrayList<HashMap>) cloudinary.api().resources(ObjectUtils.asMap("resource_type", "raw")).get("resources");
            return cloudBooks.stream().map(cloudBook -> {
                try {
                    return new URL((String) cloudBook.get("secure_url"));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

        }catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}