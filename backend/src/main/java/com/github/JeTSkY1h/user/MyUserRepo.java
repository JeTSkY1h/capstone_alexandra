package com.github.JeTSkY1h.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepo extends MongoRepository<MyUser, String> {
    Optional<MyUser>findByUsername(String username);
}
