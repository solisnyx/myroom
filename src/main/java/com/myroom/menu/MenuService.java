package com.myroom.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myroom.common.NotFoundException;
import com.myroom.menu.dto.MenuRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> findAll(MenuCategory category, Boolean availableOnly) {
        if (category != null) {
            return menuRepository.findByCategory(category);
        }
        if (Boolean.TRUE.equals(availableOnly)) {
            return menuRepository.findByAvailableTrue();
        }
        return menuRepository.findAll();
    }

    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("menu.notFound", id));
    }

    @Transactional
    public Menu create(MenuRequest request) {
        Menu menu = Menu.builder()
                .nameJa(request.getNameJa())
                .nameKo(request.getNameKo())
                .category(request.getCategory())
                .price(request.getPrice())
                .descriptionJa(request.getDescriptionJa())
                .descriptionKo(request.getDescriptionKo())
                .available(request.getAvailable() == null ? Boolean.TRUE : request.getAvailable())
                .build();
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu update(Long id, MenuRequest request) {
        Menu menu = findById(id);
        menu.setNameJa(request.getNameJa());
        menu.setNameKo(request.getNameKo());
        menu.setCategory(request.getCategory());
        menu.setPrice(request.getPrice());
        menu.setDescriptionJa(request.getDescriptionJa());
        menu.setDescriptionKo(request.getDescriptionKo());
        if (request.getAvailable() != null) {
            menu.setAvailable(request.getAvailable());
        }
        return menu;
    }

    @Transactional
    public void delete(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new NotFoundException("menu.notFound", id);
        }
        menuRepository.deleteById(id);
    }
}
