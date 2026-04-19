package com.myroom.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/logout")
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtAuthenticationFilter.AUTH_COOKIE, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
        return new RedirectView("/");
    }

    @PostMapping("/logout.json")
    public ResponseEntity<Void> logoutJson(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtAuthenticationFilter.AUTH_COOKIE, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
