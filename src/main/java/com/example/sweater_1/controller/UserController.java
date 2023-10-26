package com.example.sweater_1.controller;

import com.example.sweater_1.domain.Role;
import com.example.sweater_1.domain.User;
import com.example.sweater_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
//Для решения проблемы добавления роли "ADMIN"
//потребовалось обновить ограничения у параметра "user_role_roles_check"
//путём удаления старого ограничения и добавлением нового с помощью SQL-запроса
//
//-- Удаление текущего ограничения user_role_roles_check
//      ALTER TABLE user_role DROP CONSTRAINT user_role_roles_check;
//
//-- Добавление нового ограничения, разрешающего вставку роли 'ADMIN'
//      ALTER TABLE user_role ADD CONSTRAINT user_role_roles_check CHECK (roles::text = 'USER'::text OR roles::text = 'ADMIN'::text);
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email) {
        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }
}