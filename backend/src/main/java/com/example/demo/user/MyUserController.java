package com.example.demo.user;

import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MyUserController {
    private final  MyUserService myUserService;

    @PostMapping
    ResponseEntity<LoginResponse> register(@RequestBody MyUser newUser){
       return ResponseEntity.ok(myUserService.createNewUser(newUser));
    }

    @GetMapping
    ResponseEntity<String>getUser(Principal principal){
        return ResponseEntity.of(Optional.of(myUserService.findByUsername(principal.getName()).get().getId()));
    }
}
