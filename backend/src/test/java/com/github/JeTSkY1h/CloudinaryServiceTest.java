package com.github.JeTSkY1h;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.http42.api.Response;
import com.cloudinary.utils.ObjectUtils;
import com.github.JeTSkY1h.book.CloudinaryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudinaryServiceTest {

    Cloudinary cloudinary = Mockito.mock(Cloudinary.class, Mockito.RETURNS_DEEP_STUBS);
    CloudinaryService cloudinaryService = new CloudinaryService(cloudinary);

    @Test
    void getbookURLs() throws Exception{
        String bookpath = System.getProperty("user.dir") + File.separator + ".." + File.separator + "Books";
        URL bookURL = new URL(("file://" + bookpath + File.separator + "pg2600.epub").replace("/C:", "C:").replace("/", File.separator ));
        HashMap<String, Object> secureURL = new HashMap<>();
        secureURL.put("secure_url" ,("file://" + bookpath + File.separator + "pg2600.epub").replace("/C:", "C:").replace("/", File.separator));
        ArrayList resources = new ArrayList<HashMap>();
        resources.add(secureURL);
        Map<String, Object> urlString =  Map.of("resources",  resources);
        Mockito.when(cloudinary.api().resources(ObjectUtils.asMap("resource_type", "raw"))).thenReturn( new Response(null, urlString));

        List<URL> res = cloudinaryService.getBookURLs();

        Assertions.assertThat(res).isEqualTo(List.of(bookURL));
    }

    @Test
    void shouldUploadCover(){

    }
}
