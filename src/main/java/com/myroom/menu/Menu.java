package com.myroom.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ja", nullable = false, length = 200)
    private String nameJa;

    @Column(name = "name_ko", length = 200)
    private String nameKo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuCategory category;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "description_ja", length = 500)
    private String descriptionJa;

    @Column(name = "description_ko", length = 500)
    private String descriptionKo;

    @Column(nullable = false)
    private Boolean available;
}
