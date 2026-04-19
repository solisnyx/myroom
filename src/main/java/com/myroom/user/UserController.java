package com.myroom.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myroom.auth.AuthPrincipal;
import com.myroom.user.dto.UserResponse;
import com.myroom.user.dto.UserUpdateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal AuthPrincipal principal) {
        return UserResponse.from(userService.findById(principal.id()));
    }

    @PutMapping("/me")
    public UserResponse updateMe(@AuthenticationPrincipal AuthPrincipal principal,
                                  @Valid @RequestBody UserUpdateRequest request) {
        return UserResponse.from(userService.updateProfile(principal.id(), request));
    }
}
