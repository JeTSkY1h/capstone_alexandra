package com.example.demo.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "User")
public class MyUser {
    @Id
    String Id;
    String username;
    String password;
    List<String> roles;
}
