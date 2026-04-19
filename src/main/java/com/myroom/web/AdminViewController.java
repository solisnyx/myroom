package com.myroom.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myroom.auth.AuthPrincipal;
import com.myroom.menu.MenuCategory;
import com.myroom.menu.MenuService;
import com.myroom.menu.dto.MenuResponse;
import com.myroom.reservation.ReservationService;
import com.myroom.reservation.ReservationStatus;
import com.myroom.reservation.dto.ReservationResponse;
import com.myroom.room.RoomRepository;
import com.myroom.user.UserRole;
import com.myroom.user.UserService;
import com.myroom.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final MenuService menuService;
    private final RoomRepository roomRepository;
    private final ReservationService reservationService;
    private final UserService userService;

    @ModelAttribute("principal")
    AuthPrincipal principal(@AuthenticationPrincipal AuthPrincipal p) {
        return p;
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/menus")
    public String menus(Model model) {
        model.addAttribute("menus", menuService.findAll(null, null).stream()
                .map(MenuResponse::from).toList());
        model.addAttribute("categories", MenuCategory.values());
        return "admin/menus";
    }

    @GetMapping("/menus/new")
    public String menuNew(Model model) {
        model.addAttribute("categories", MenuCategory.values());
        return "admin/menu-form";
    }

    @GetMapping("/menus/{id}/edit")
    public String menuEdit(@PathVariable Long id, Model model) {
        model.addAttribute("menu", MenuResponse.from(menuService.findById(id)));
        model.addAttribute("categories", MenuCategory.values());
        return "admin/menu-form";
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        return "admin/rooms";
    }

    @GetMapping("/rooms/new")
    public String roomNew() {
        return "admin/room-form";
    }

    @GetMapping("/rooms/{id}/edit")
    public String roomEdit(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomRepository.findById(id).orElseThrow());
        return "admin/room-form";
    }

    @GetMapping("/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations",
                reservationService.findAll(null, null, null).stream()
                        .map(ReservationResponse::from).toList());
        model.addAttribute("statuses", ReservationStatus.values());
        return "admin/reservations";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll().stream()
                .map(UserResponse::from).toList());
        model.addAttribute("roles", UserRole.values());
        return "admin/users";
    }
}
