package com.myroom.menu.dto;

import com.myroom.menu.Menu;
import com.myroom.menu.MenuCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String nameJa;
    private String nameKo;
    private MenuCategory category;
    private Integer price;
    private String descriptionJa;
    private String descriptionKo;
    private Boolean available;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .nameJa(menu.getNameJa())
                .nameKo(menu.getNameKo())
                .category(menu.getCategory())
                .price(menu.getPrice())
                .descriptionJa(menu.getDescriptionJa())
                .descriptionKo(menu.getDescriptionKo())
                .available(menu.getAvailable())
                .build();
    }
}
