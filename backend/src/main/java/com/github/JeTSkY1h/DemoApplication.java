package com.github.JeTSkY1h;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

    @Bean
    Cloudinary cloudinary(  @Value("${cloudinary.api.key}") String key, @Value("${cloudinary.api.secret}") String secret){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djxtc8lav",
                "api_key", key,
                "api_secret", secret));
    }
}
