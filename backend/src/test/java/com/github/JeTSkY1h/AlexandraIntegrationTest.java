package com.github.JeTSkY1h;

import com.github.JeTSkY1h.user.LoginData;
import com.github.JeTSkY1h.user.LoginResponse;
import com.github.JeTSkY1h.user.MyUser;
import org.apache.juli.logging.Log;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlexandraIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void AlexandraIntegrationtest(){
        var newUser = new LoginData("testUser", "testPassword");
        //create User
        ResponseEntity<LoginResponse> createUserResponse = restTemplate.postForEntity(" /api/user/", newUser, LoginResponse.class);
        Assertions.assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        //failed Login
        ResponseEntity<LoginResponse> failedLoginResponse = restTemplate.postForEntity("/api/auth", new LoginData("wronguser", "wrongpassword"), LoginResponse.class);
        Assertions.assertThat(failedLoginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        //login
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/api/login", new LoginData("testUser", "testPassword"), LoginResponse.class);
        Assertions.assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwt = loginResponse.getBody().getToken();

        Assertions.assertThat(jwt).isNotBlank();


    }

}
