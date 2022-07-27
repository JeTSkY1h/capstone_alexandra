package com.github.JeTSkY1h.security;

import com.github.JeTSkY1h.user.MyUser;
import com.github.JeTSkY1h.user.MyUserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MyUserService myUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  throws ServletException, IOException {

        String token = getToken(request);
        if (token != null && !token.isBlank()) {
            try {
                Claims claims = jwtService.extractAllClaims(token);
                System.out.println(claims);
                setSecurityContext(claims);
            } catch (Exception e) {
                response.setStatus(401);
            }
        }
        filterChain.doFilter(request, response);
    }
    
    private String getToken(HttpServletRequest request) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                return authorization.replace("Bearer", "").trim();
            }
            return null;
        }

    private void setSecurityContext(Claims claims) {

        List<SimpleGrantedAuthority> grantedAuthorities = claims.get("roles") == null ? List.of() :
                ((List<String>) claims.get("roles")).stream().map(au -> new SimpleGrantedAuthority("ROLE_" + au)).toList();
        MyUser user = myUserService.findByUsername(claims.getSubject()).orElseThrow();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), "", grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}




