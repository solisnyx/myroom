package com.myroom.auth;

import com.myroom.user.User;
import com.myroom.user.UserRole;

public record AuthPrincipal(Long id, String email, String nickname, UserRole role) {

    public static AuthPrincipal from(User user) {
        return new AuthPrincipal(user.getId(), user.getEmail(), user.getNickname(), user.getRole());
    }

    public String authority() {
        return role.authority();
    }
}
