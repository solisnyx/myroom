package com.myroom.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByCategory(MenuCategory category);
    List<Menu> findByAvailableTrue();
}
