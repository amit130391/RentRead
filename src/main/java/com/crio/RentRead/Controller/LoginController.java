package com.crio.RentRead.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Your username or password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login"; // Refers to login.html in src/main/resources/templates/
    }

    @GetMapping("/welcome")
    public String welcome(Model model,@AuthenticationPrincipal UserDetails userdetails) {
        model.addAttribute("username", userdetails.getUsername());
        return "welcome"; // This should match the Thymeleaf template name (welcome.html)
    }
}
