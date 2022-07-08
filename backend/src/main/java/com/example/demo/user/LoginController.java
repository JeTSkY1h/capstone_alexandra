package com.example.demo.user;

import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final MyUserService myUserService;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<LoginResponse> login(@RequestBody LoginData login){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            MyUser user = myUserService.findByUsername(login.getUsername()).orElseThrow();
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", user.getRoles());
            return ResponseEntity.ok(new LoginResponse(jwtService.createJwt(claims, login.getUsername())));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
