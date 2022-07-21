package com.github.JeTSkY1h.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    @PutMapping("/roles")
    ResponseEntity<Object> giveAdmin(@RequestBody MyUser newAdmin){
        return ResponseEntity.ok(myUserService.giveAdmin(newAdmin));
    }

    @GetMapping
    ResponseEntity<String>getUser(Principal principal){
        return ResponseEntity.of(Optional.of(myUserService.findByUsername(principal.getName()).orElseThrow().getId()));
    }

    @GetMapping("/bookdata")
    ResponseEntity<List<BookUserData>>getBookUserData(Principal principal){
        Optional<MyUser> user = myUserService.findByUsername(principal.getName());
        if(user.isEmpty()) {
            return null;
        }
        return ResponseEntity.ok(user.get().getBookData());
    }

    @PutMapping("/bookdata")
    ResponseEntity<List<BookUserData>>setBookUserData(Principal principal, @RequestBody BookUserData bookdata){
        return ResponseEntity.of(Optional.of(myUserService.setBookUserData(principal.getName(), bookdata)));
    }
}
