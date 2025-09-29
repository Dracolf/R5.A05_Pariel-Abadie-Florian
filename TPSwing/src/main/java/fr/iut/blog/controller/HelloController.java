package fr.iut.blog.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/bonjour")
    public String bonjour() {
        return "Bonjour le monde !";
    }
}

