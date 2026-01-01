package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;

    public AdminController(UserService userServiceImpl,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping
    public String admin(Model model, Authentication authentication) {
        fillHeader(model, authentication);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleService.findAll());
        return "users-list";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model, Authentication authentication) {
        fillHeader(model, authentication);
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "user-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") User user,
                       @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                       @RequestParam(value = "rawPassword", required = false) String rawPassword) {
        userService.saveUser(user, roleIds, rawPassword);
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    private void fillHeader(Model model, Authentication authentication) {
        if (authentication == null) {
            model.addAttribute("currentEmail", "");
            model.addAttribute("currentRoles", "");
            return;
        }
        model.addAttribute("currentEmail", authentication.getName());
        model.addAttribute("currentRoles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" ")));
    }
}