package com.github.JeTSkY1h.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "User")
public class MyUser {
    @Id
    String id;
    String username;
    String password;
    List<String> roles;
    List<BookUserData> bookData;
}
