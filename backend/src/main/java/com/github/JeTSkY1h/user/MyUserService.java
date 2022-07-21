package com.github.JeTSkY1h.user;

import com.github.JeTSkY1h.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final MyUserRepo myUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    LoginResponse createNewUser(MyUser newUser){
        String hashedPw = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPw);
        newUser.setRoles(List.of("user"));
        MyUser user = myUserRepo.save(newUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        return new LoginResponse(jwtService.createJwt(claims, user.getUsername()));
    }

    public Optional<MyUser> findById(String id){
        return myUserRepo.findById(id);
    }

    public Optional<MyUser> findByUsername(String username){
        return myUserRepo.findByUsername(username);
    }

    public Object giveAdmin(MyUser newAdmin) {
        List<String> currRoles = newAdmin.getRoles();
        currRoles.add("admin");
        newAdmin.setRoles(currRoles);
        return myUserRepo.save(newAdmin);
    }

    public Optional<List<BookUserData>> getBookuserData(String name) {
        MyUser user = findByUsername(name).orElseThrow();
        try{
            return Optional.of(user.getBookData());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public List<BookUserData> setBookUserData(String name, BookUserData bookData) {
        MyUser user = findByUsername(name).orElseThrow();
        List<BookUserData> currData = user.getBookData() == null ? new ArrayList<>(): user.getBookData();
        if(!currData.isEmpty()){
            Optional<BookUserData> data = currData.stream().filter(bookUserData -> bookUserData.getBookId().equals(bookData.getBookId())).findFirst();
            data.ifPresent(currData::remove);
        }
        currData.add(bookData);
        user.setBookData(currData);
        myUserRepo.save(user);
        return currData;
    }
}
