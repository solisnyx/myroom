package com.myroom.menu.dto;

import com.myroom.menu.MenuCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequest {

    @NotBlank(message = "{menu.nameJa.required}")
    @Size(max = 200)
    private String nameJa;

    @Size(max = 200)
    private String nameKo;

    @NotNull(message = "{menu.category.required}")
    private MenuCategory category;

    @NotNull(message = "{menu.price.required}")
    @PositiveOrZero(message = "{menu.price.positive}")
    private Integer price;

    @Size(max = 500)
    private String descriptionJa;

    @Size(max = 500)
    private String descriptionKo;

    private Boolean available = Boolean.TRUE;
}
