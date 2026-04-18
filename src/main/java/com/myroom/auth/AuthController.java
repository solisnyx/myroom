package com.myroom.auth;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class AuthController {

    @GetMapping("/success")
    public Map<String, String> success(@RequestParam String token) {
        return Map.of(
                "tokenType", "Bearer",
                "accessToken", token);
    }
}
