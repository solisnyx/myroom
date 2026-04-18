package com.myroom.user.dto;

import com.myroom.user.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleUpdateRequest {

    @NotNull(message = "{user.role.required}")
    private UserRole role;
}
