package com.github.JeTSkY1h;

import com.github.JeTSkY1h.book.Book;
import com.github.JeTSkY1h.user.LoginData;
import com.github.JeTSkY1h.user.LoginResponse;
import com.github.JeTSkY1h.user.MyUser;
import com.github.JeTSkY1h.user.MyUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlexandraIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    MyUserService myUserService;

    @Test
    void AlexandraIntegrationtest(){



        var newUser = new LoginData("testUser", "testPassword");
        //create User
        ResponseEntity<LoginResponse> createUserResponse = restTemplate.postForEntity("/api/user", newUser, LoginResponse.class);
        Assertions.assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        //failed Login
        ResponseEntity<LoginResponse> failedLoginResponse = restTemplate.postForEntity("/api/auth", new LoginData("wronguser", "wrongpassword"), LoginResponse.class);
        Assertions.assertThat(failedLoginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        //login
        ResponseEntity<LoginResponse> loginResponseNoAdmin = restTemplate.postForEntity("/api/auth", new LoginData("testUser", "testPassword"), LoginResponse.class);
        Assertions.assertThat(loginResponseNoAdmin.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwtNoAdmin = loginResponseNoAdmin.getBody().getToken();

        Assertions.assertThat(jwtNoAdmin).isNotBlank();

        //try to refresh with no admin User
        ResponseEntity<Void> refreshTry = restTemplate.exchange("/api/books/refresh",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtNoAdmin)),
                Void.class
        );
        Assertions.assertThat(refreshTry.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        //give user Admin Rights
        ResponseEntity<String> userId = restTemplate.exchange("/api/user/",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtNoAdmin)),
                String.class
        );
        MyUser testUser = myUserService.findByUsername("testUser").orElseThrow();
        myUserService.giveAdmin(testUser);
        MyUser testUserAdmin = myUserService.findByUsername("testUser").orElseThrow();
        Assertions.assertThat(testUserAdmin.getRoles()).contains("admin");

        //generate new login token
        ResponseEntity<LoginResponse> loginResponseAdmin = restTemplate.postForEntity("/api/auth", new LoginData("testUser", "testPassword"), LoginResponse.class);
        Assertions.assertThat(loginResponseAdmin.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwtAdmin = loginResponseAdmin.getBody().getToken();


        //Refresh book list
        ResponseEntity<Void> refresh = restTemplate.exchange("/api/books/refresh",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                Void.class
        );
        Assertions.assertThat(refresh.getStatusCode()).isEqualTo(HttpStatus.OK);

        //get books
        ResponseEntity<Book[]> books = restTemplate.exchange("/api/books",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwtAdmin)),
                Book[].class
        );
        System.out.println(books.getBody()[0]);
        Assertions.assertThat(books.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(books.getBody()[0].getTitle()).isEqualTo("War and Peace");

    }

    final HttpHeaders createHeaders(String jwt) {
        String authHeaderValue = "Bearer " + jwt;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeaderValue);
        return headers;
    }
}
