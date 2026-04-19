package com.myroom.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.myroom.auth.AuthPrincipal;
import com.myroom.menu.MenuCategory;
import com.myroom.menu.MenuService;
import com.myroom.menu.dto.MenuResponse;
import com.myroom.reservation.ReservationService;
import com.myroom.reservation.dto.ReservationResponse;
import com.myroom.room.RoomRepository;
import com.myroom.user.UserService;
import com.myroom.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final MenuService menuService;
    private final RoomRepository roomRepository;
    private final ReservationService reservationService;
    private final UserService userService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("rooms", roomRepository.findAll());
        return "home";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal AuthPrincipal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/menus")
    public String menus(@AuthenticationPrincipal AuthPrincipal principal,
                        @RequestParam(required = false) MenuCategory category,
                        Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("menus", menuService.findAll(category, Boolean.TRUE).stream()
                .map(MenuResponse::from).toList());
        model.addAttribute("category", category);
        model.addAttribute("categories", MenuCategory.values());
        return "menus";
    }

    @GetMapping("/rooms")
    public String rooms(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("rooms", roomRepository.findAll());
        return "rooms";
    }

    @GetMapping("/reservations/new")
    public String reservationForm(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("rooms", roomRepository.findAll());
        return "reservation-new";
    }

    @GetMapping("/reservations/me")
    public String myReservations(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("reservations", reservationService.findMine(principal.id()).stream()
                .map(ReservationResponse::from).toList());
        return "reservations-me";
    }

    @GetMapping("/reservations/{id}")
    public String reservationDetail(@PathVariable Long id,
                                     @AuthenticationPrincipal AuthPrincipal principal,
                                     Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("reservation",
                ReservationResponse.from(reservationService.findAccessible(id, principal)));
        model.addAttribute("rooms", roomRepository.findAll());
        return "reservation-detail";
    }

    @GetMapping("/me")
    public String profile(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("user", UserResponse.from(userService.findById(principal.id())));
        return "profile";
    }
}
