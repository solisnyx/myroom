package com.myroom.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Size(max = 80, message = "{user.nickname.size}")
    private String nickname;

    @Size(max = 30)
    private String phone;
}
