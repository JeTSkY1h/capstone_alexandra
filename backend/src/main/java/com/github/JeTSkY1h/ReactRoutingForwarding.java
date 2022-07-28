package com.github.JeTSkY1h;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReactRoutingForwarding {
    @GetMapping(value = "/**/{[path:[^\\.]*}")
    public String forwardToRouteUrl() {
        return "forward:/";
    }

    @GetMapping("/")
    public String asdsad() {
        return "forward:/";
    }
}