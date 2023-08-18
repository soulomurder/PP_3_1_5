package ex.pp_3_1_5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ex.pp_3_1_5.model.User;
import ex.pp_3_1_5.service.UserDetailsServiceImpl;
import ex.pp_3_1_5.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String admin() {
        return "admin/adminpage";
    }
}
