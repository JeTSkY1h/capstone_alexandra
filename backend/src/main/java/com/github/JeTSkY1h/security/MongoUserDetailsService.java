package com.github.JeTSkY1h.security;

import com.github.JeTSkY1h.user.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {

    private final MyUserRepo myUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return myUserRepo.findByUsername(username).map(user -> new User(user.getUsername(), user.getPassword(), List.of()))
                .orElseThrow(()-> new UsernameNotFoundException(username + " not found"));
    }
}
