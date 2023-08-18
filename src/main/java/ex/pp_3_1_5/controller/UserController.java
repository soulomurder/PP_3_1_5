package ex.pp_3_1_5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ex.pp_3_1_5.service.UserDetailsServiceImpl;
import ex.pp_3_1_5.service.UserService;
import java.security.Principal;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(UserService userService,
                          UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public String showUser(Model model, Principal principal) {
        model.addAttribute("user", userDetailsService.findByUserName(principal.getName()));
        return "user/userpage";
    }
}
