package com.github.JeTSkY1h.user;

import com.github.JeTSkY1h.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
}
