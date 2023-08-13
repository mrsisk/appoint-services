package mrsisk.github.io.appointservice.controllers;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedule/test")
public class TestController {


    @GetMapping
    String test(JwtAuthenticationToken token){
        System.out.println(token);
        return "Hello sisk" ;
    }

}
